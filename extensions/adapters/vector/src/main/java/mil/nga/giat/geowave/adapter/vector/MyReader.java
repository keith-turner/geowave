package mil.nga.giat.geowave.adapter.vector;

import mil.nga.giat.geowave.core.store.data.field.FieldReader;

public class MyReader implements
		FieldReader<Object>
{

	@Override
	public Object readField(
			final byte[] fieldData ) {

		// the class name is not prefaced in the payload, we are assuming it is
		// a raster tile implementation and instantiating it directly

//		final Object retVal = PersistenceUtils.classFactory(
//				SimpleFeature.class.getName(),
//				SimpleFeature.class);
//		if (retVal != null) {
//			retVal = fieldData;//.fromBinary(fieldData);
//		}
		return (Object) fieldData;
				
//		return retVal;
	}

}
