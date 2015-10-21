package mil.nga.giat.geowave.adapter.vector;

import mil.nga.giat.geowave.core.index.ByteArrayId;
import mil.nga.giat.geowave.core.store.data.field.FieldWriter;

import org.opengis.feature.simple.SimpleFeature;

public class MyWriter implements
		FieldWriter<SimpleFeature, Object>
{
	@Override
	public byte[] getVisibility(
			final SimpleFeature rowValue,
			final ByteArrayId fieldId,
			final Object fieldValue ) {
		return new byte[] {};
	}

	@Override
	public byte[] writeField(
			final Object fieldValue ) {
		// there is no need to preface the payload with the class name and a
		// length of the class name, the implementation is assumed to be known
		// on read so we can save space on persistence
		return (byte[]) fieldValue;
	}

}
