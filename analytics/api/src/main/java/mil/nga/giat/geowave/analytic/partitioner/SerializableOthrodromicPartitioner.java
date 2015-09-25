package mil.nga.giat.geowave.analytic.partitioner;

import java.io.IOException;

import javax.measure.quantity.Length;
import javax.measure.unit.Unit;

import org.opengis.referencing.crs.CoordinateReferenceSystem;

import mil.nga.giat.geowave.analytic.ConfigurationWrapper;
import mil.nga.giat.geowave.analytic.extract.DimensionExtractor;
import mil.nga.giat.geowave.analytic.partitioner.OrthodromicDistancePartitioner;
import mil.nga.giat.geowave.core.store.index.CommonIndexModel;

public class SerializableOthrodromicPartitioner<T> extends
		OrthodromicDistancePartitioner<T> implements
		java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConfigurationWrapper myConfiguration;

	public SerializableOthrodromicPartitioner() {
		super();
	}

	public SerializableOthrodromicPartitioner(
			CoordinateReferenceSystem crs,
			CommonIndexModel indexModel,
			DimensionExtractor<T> dimensionExtractor,
			double[] distancePerDimension,
			Unit<Length> geometricDistanceUnit ) {
		super(
				crs,
				indexModel,
				dimensionExtractor,
				distancePerDimension,
				geometricDistanceUnit);
	}

	@Override
	public void initialize(
			final ConfigurationWrapper context )
			throws IOException {
		myConfiguration = context;
		super.initialize(context);
	}

	private void writeObject(
			java.io.ObjectOutputStream out )
			throws IOException {
		out.writeObject(myConfiguration);
	}

	private void readObject(
			java.io.ObjectInputStream in )
			throws IOException,
			ClassNotFoundException {
		initialize((ConfigurationWrapper) in.readObject());
	}
}
