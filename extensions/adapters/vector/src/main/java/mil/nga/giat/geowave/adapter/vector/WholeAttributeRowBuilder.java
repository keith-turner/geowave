package mil.nga.giat.geowave.adapter.vector;

import java.util.HashMap;

import mil.nga.giat.geowave.adapter.vector.simpleFeature.avro.TypeConverter;
import mil.nga.giat.geowave.core.index.ByteArrayId;
import mil.nga.giat.geowave.core.index.StringUtils;
import mil.nga.giat.geowave.core.store.adapter.NativeFieldHandler.RowBuilder;
import mil.nga.giat.geowave.core.store.data.PersistentValue;

import org.apache.log4j.Logger;
import org.opengis.feature.simple.SimpleFeature;

/**
 * A GeoWave RowBuilder, used internally by AbstractDataAdapter to construct
 * rows from a set field values (in this case SimpleFeatures from a set of
 * attribute values). This implementation simply wraps a geotools
 * SimpleFeatureBuilder.
 * 
 */
public class WholeAttributeRowBuilder implements
		RowBuilder<SimpleFeature, Object>
{
	private final HashMap<String, Object> idToValue = new HashMap<String, Object>();

	private ByteArrayId id;

	private final static Logger LOGGER = Logger.getLogger(WholeAttributeRowBuilder.class);

	public WholeAttributeRowBuilder(ByteArrayId byteArrayId) {
		this.id = byteArrayId;
	}

	@Override
	public SimpleFeature buildRow(
			final ByteArrayId dataId ) {
		TypeConverter tc = new TypeConverter();
		SimpleFeature deserializedSimpleFeature = null;
		try {
			System.out.println(dataId.toString());
			deserializedSimpleFeature = tc.deserializeAvroSimpleFeature((byte[]) idToValue.get(StringUtils.stringFromBinary(id.getBytes())));
		}
		catch (Exception e) {
			LOGGER.error(
					"Unable to deserialize SimpleFeature using dataId '" + dataId.toString() + "'",
					e);
		}

		return deserializedSimpleFeature;
	}

	@Override
	public void setField(
			final PersistentValue<Object> fieldValue ) {
		idToValue.put(
				StringUtils.stringFromBinary(id.getBytes()),
				fieldValue.getValue());
	}
}