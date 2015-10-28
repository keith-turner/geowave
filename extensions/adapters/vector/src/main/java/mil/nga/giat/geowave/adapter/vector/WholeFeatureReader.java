package mil.nga.giat.geowave.adapter.vector;

import mil.nga.giat.geowave.core.store.data.field.FieldReader;

public class WholeFeatureReader implements
		FieldReader<Object>
{

	@Override
	public Object readField(
			final byte[] fieldData ) {
		return (Object) fieldData;
	}
}
