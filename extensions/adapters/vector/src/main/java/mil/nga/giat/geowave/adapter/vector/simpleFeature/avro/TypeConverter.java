package mil.nga.giat.geowave.adapter.vector.simpleFeature.avro;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mil.nga.giat.geowave.core.store.data.field.FieldReader;
import mil.nga.giat.geowave.core.store.data.field.FieldUtils;
import mil.nga.giat.geowave.core.store.data.field.FieldWriter;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import com.google.common.base.Preconditions;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKBWriter;

/**
 * Note - class is not threadsafe
 */
public class TypeConverter
{
	private final EncoderFactory ef = EncoderFactory.get();
	private final DecoderFactory df = DecoderFactory.get();
	private final SpecificDatumWriter<AvroSimpleFeature> datumWriter = new SpecificDatumWriter<AvroSimpleFeature>();
	private final SpecificDatumReader<AvroSimpleFeature> datumReader = new SpecificDatumReader<AvroSimpleFeature>();
	private final WKBWriter wkbWriter = new WKBWriter(
			3);
	private final WKBReader wkbReader = new WKBReader();

	/***
	 * @param avroObject
	 *            Avro object to serialized
	 * @return byte array of serialized avro data
	 * @throws IOException
	 */
	private byte[] serializeAvroSimpleFeature(
			final AvroSimpleFeature avroObject )
			throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		BinaryEncoder encoder = ef.binaryEncoder(
				os,
				null);
		datumWriter.setSchema(avroObject.getSchema());
		datumWriter.write(
				avroObject,
				encoder);
		encoder.flush();
		return os.toByteArray();
	}

	/***
	 * Converts a SimpleFeature to an avroSimpleFeature and then serializes it.
	 * 
	 * @param sf
	 *            Simple Feature to be serialized
	 * @param avroObjectToReuse
	 *            null or AvroSimpleFeature instance to be re-used. If null a
	 *            new instance will be allocated
	 * @param defaultClassifications
	 *            null map of attribute names vs. classification. if null all
	 *            values will be set to the default classification
	 * @param defaultClassification
	 *            null or default classification. if null and
	 *            defaultClassifications are not provided an exception will be
	 *            thrown
	 * @return
	 * @throws IOException
	 */
	public byte[] serializeAvroSimpleFeature(
			SimpleFeature sf,
			AvroSimpleFeature avroObjectToReuse,
			Map<String, String> defaultClassifications,
			String defaultClassification )
			throws IOException {
		if (sf == null) {
			throw new IOException(
					"Feature cannot be null");
		}

		if (defaultClassification == null && defaultClassifications == null) {
			throw new IOException(
					"if per attribute classifications aren't provided then a default classification must be provided");
		}

		SimpleFeatureType sft = sf.getType();
		if (avroObjectToReuse == null) {
			avroObjectToReuse = new AvroSimpleFeature();
		}

		FeatureDefinition fd = buildFeatureDefinition(
				avroObjectToReuse.getFeatureType(),
				sft,
				defaultClassifications,
				defaultClassification);
		avroObjectToReuse.setFeatureType(fd);

		AttributeValue av = buildAttributeValue(
				sf,
				sft);
		avroObjectToReuse.setValue(av);

		return serializeAvroSimpleFeature(avroObjectToReuse);
	}

	/**
	 * Add the attributes, types and classifications for the SimpleFeatureType
	 * to the provided FeatureDefinition
	 * 
	 * @param fd
	 *            - existing Feature Definition (or new one if null)
	 * @param sft
	 *            - SimpleFeatureType of the simpleFeature being serialized
	 * @param defaultClassifications
	 *            - map of attribute names to classification
	 * @param defaultClassification
	 *            - default classification if one could not be found in the map
	 * @return
	 * @throws IOException
	 */
	private FeatureDefinition buildFeatureDefinition(
			FeatureDefinition fd,
			SimpleFeatureType sft,
			Map<String, String> defaultClassifications,
			String defaultClassification )
			throws IOException {
		if (fd == null) {
			fd = new FeatureDefinition();
		}
		fd.setFeatureTypeName(sft.getTypeName());

		List<String> attributes = new ArrayList<String>(
				sft.getAttributeCount());
		List<String> types = new ArrayList<String>(
				sft.getAttributeCount());
		List<String> classifications = new ArrayList<String>(
				sft.getAttributeCount());

		for (AttributeDescriptor attr : sft.getAttributeDescriptors()) {
			String localName = attr.getLocalName();

			attributes.add(localName);
			types.add(attr.getType().getBinding().getCanonicalName());
			classifications.add(getClassification(
					localName,
					defaultClassifications,
					defaultClassification));
		}

		fd.setAttributeNames(attributes);
		fd.setAttributeTypes(types);
		fd.setAttributeDefaultClassifications(classifications);

		return fd;
	}

	/**
	 * If a classification exists for this attribute name then use it If not
	 * then use the provided default classification
	 * 
	 * @param localName
	 *            - attribute name
	 * @param defaultClassifications
	 *            - map of attribute names to classification
	 * @param defaultClassification
	 *            - default classification to use if one is not mapped for the
	 *            name provided
	 * @return
	 * @throws IOException
	 */
	private String getClassification(
			String localName,
			Map<String, String> defaultClassifications,
			String defaultClassification )
			throws IOException {
		String classification = null;

		if (defaultClassifications != null && defaultClassifications.containsKey(localName)) {
			classification = defaultClassifications.get(localName);
		}
		else {
			classification = defaultClassification;
		}

		if (classification == null) {
			throw new IOException(
					"No default classification was provided, and no classification for: '" + localName + "' was provided");
		}

		return classification;
	}

	/**
	 * Create an AttributeValue from the SimpleFeature's attributes
	 * 
	 * @param sf
	 * @param sft
	 * @return
	 */
	private AttributeValue buildAttributeValue(
			SimpleFeature sf,
			SimpleFeatureType sft ) {
		AttributeValue attributeValue = new AttributeValue();

		List<ByteBuffer> values = new ArrayList<ByteBuffer>(
				sft.getAttributeCount());

		attributeValue.setFid(sf.getID());

		for (AttributeDescriptor attr : sft.getAttributeDescriptors()) {
			Object o = sf.getAttribute(attr.getLocalName());
			byte[] bytes = null;
			if (o instanceof Geometry) {
				bytes = wkbWriter.write((Geometry) o);
			}
			else {
				FieldWriter fw = FieldUtils.getDefaultWriterForClass(attr.getType().getBinding());
				bytes = fw.writeField(o);
			}
			values.add(ByteBuffer.wrap(bytes));
		}
		attributeValue.setValues(values);

		return attributeValue;
	}

	/***
	 * Deserialize byte stream into an AvroSimpleFeature
	 * 
	 * @param avroData
	 *            serialized bytes of AvroSimpleFeature
	 * @param avroObjectToReuse
	 *            null or AvroSimpleFeature instance to be re-used. If null a
	 *            new object will be allocated.
	 * @return instance of AvroSimpleFeature with values parsed from avroData
	 * @throws IOException
	 */
	private AvroSimpleFeature deserializeASF(
			final byte[] avroData,
			AvroSimpleFeature avroObjectToReuse )
			throws IOException {
		BinaryDecoder decoder = df.binaryDecoder(
				avroData,
				null);
		if (avroObjectToReuse == null) {
			avroObjectToReuse = new AvroSimpleFeature();
		}

		datumReader.setSchema(avroObjectToReuse.getSchema());
		return datumReader.read(
				avroObjectToReuse,
				decoder);
	}

	/***
	 * Deserialize byte array into an AvroSimpleFeature then convert to a
	 * SimpleFeature
	 * 
	 * @param avroData
	 *            serialized bytes of a AvroSimpleFeature
	 * @return Collection of GeoTools SimpleFeature instances.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws ParseException
	 */
	public SimpleFeature deserializeAvroSimpleFeature(
			final byte[] avroData )
			throws IOException,
			ClassNotFoundException,
			ParseException {
		// Deserialize
		AvroSimpleFeature sfc = deserializeASF(
				avroData,
				null);

		// Convert
		SimpleFeature simpleFeature = null;

		SimpleFeatureTypeBuilder sftb = new SimpleFeatureTypeBuilder();
		sftb.setName(sfc.getFeatureType().getFeatureTypeName());
		List<String> featureTypes = sfc.getFeatureType().getAttributeTypes();
		List<String> featureNames = sfc.getFeatureType().getAttributeNames();
		for (int i = 0; i < sfc.getFeatureType().getAttributeNames().size(); i++) {
			String type = featureTypes.get(i);
			String name = featureNames.get(i);
			Class c = Class.forName(type);
			sftb.add(
					name,
					c);
		}

		SimpleFeatureType sft = sftb.buildFeatureType();
		SimpleFeatureBuilder sfb = new SimpleFeatureBuilder(
				sft);

		AttributeValue attributeValue = sfc.getValue();

		// null values should still take a place in the array - check
		Preconditions.checkArgument(featureNames.size() == attributeValue.getValues().size());
		for (int i = 0; i < attributeValue.getValues().size(); i++) {
			ByteBuffer val = attributeValue.getValues().get(
					i);

			if (featureTypes.get(
					i).equals(
					"com.vividsolutions.jts.geom.Geometry")) {
				sfb.add(wkbReader.read(val.array()));
			}
			else {
				FieldReader fr = FieldUtils.getDefaultReaderForClass(Class.forName(featureTypes.get(i)));
				sfb.add(fr.readField(val.array()));
			}
		}

		simpleFeature = sfb.buildFeature(attributeValue.getFid());
		return simpleFeature;
	}
}