package mil.nga.giat.geowave.adapter.vector;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import mil.nga.giat.geowave.adapter.vector.types.generated.TypeConverter;
import mil.nga.giat.geowave.core.index.ByteArrayId;
import mil.nga.giat.geowave.core.index.StringUtils;
import mil.nga.giat.geowave.core.store.adapter.NativeFieldHandler.RowBuilder;
import mil.nga.giat.geowave.core.store.data.PersistentValue;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.io.ParseException;

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
//	private final SimpleFeatureBuilder builder;

	public WholeAttributeRowBuilder(
			final SimpleFeatureType type ) {
//		builder = new SimpleFeatureBuilder(
//				type);
	}

	@Override
	public SimpleFeature buildRow(
			final ByteArrayId dataId ) {
		TypeConverter tc = new TypeConverter();
		SimpleFeature deserializedSimpleFeature = null;
		try {
			List<SimpleFeature> features = tc.deserializeSingleFeatureCollection((byte[]) idToValue.get(StringUtils.stringFromBinary(dataId.getBytes())));
			System.out.println("Size of collection = " + features.size());
			deserializedSimpleFeature = features.get(0);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		builder.buildFeature(id)
		return deserializedSimpleFeature;//builder.buildFeature(StringUtils.stringFromBinary(dataId.getBytes()));
	}

	@Override
	public void setField(
			final PersistentValue<Object> fieldValue ) {
		idToValue.put(StringUtils.stringFromBinary(fieldValue.getId().getBytes()),
				fieldValue.getValue());
	}
}
