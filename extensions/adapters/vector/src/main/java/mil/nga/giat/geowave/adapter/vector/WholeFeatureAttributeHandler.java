package mil.nga.giat.geowave.adapter.vector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import mil.nga.giat.geowave.adapter.vector.types.generated.TypeConverter;
import mil.nga.giat.geowave.core.index.ByteArrayId;
import mil.nga.giat.geowave.core.index.StringUtils;
import mil.nga.giat.geowave.core.store.adapter.NativeFieldHandler;

import org.apache.log4j.Logger;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;

/**
 * This is used by the FeatureDataAdapter to handle GeoWave 'fields' using
 * SimpleFeature 'attributes.'
 * 
 */
public class WholeFeatureAttributeHandler implements
		NativeFieldHandler<SimpleFeature, Object>
{
	protected final ByteArrayId FIELD_ID = new ByteArrayId("foo");
	protected final AttributeDescriptor attrDesc;
	private Name name;
	
	private final static Logger LOGGER = Logger.getLogger(WholeFeatureAttributeHandler.class);

	public WholeFeatureAttributeHandler(final AttributeDescriptor attrDesc, Name name ) {
		this.attrDesc = attrDesc;
		this.name = name;
	}

	@Override
	public ByteArrayId getFieldId() {
		return new ByteArrayId(
				StringUtils.stringToBinary(name.getLocalPart()));
	}

	@Override
	public Object getFieldValue(
			final SimpleFeature row ) {
		TypeConverter tc = new TypeConverter();
		byte[] serializedAttributes = null;
		try {
			serializedAttributes = tc.serializeSingleFeatureCollection(new ArrayList<SimpleFeature>(Arrays.asList(row)), null, null, "");
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error("Error, failed to serialize SimpleFeature with id '" + row.getID() + "'", e);
		}
		
		return serializedAttributes;
	}
}