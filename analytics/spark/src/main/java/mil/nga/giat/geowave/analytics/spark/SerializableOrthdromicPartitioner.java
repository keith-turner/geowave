package mil.nga.giat.geowave.analytics.spark;

import java.io.IOException;

import mil.nga.giat.geowave.analytic.ConfigurationWrapper;
import mil.nga.giat.geowave.analytic.partitioner.OrthodromicDistancePartitioner;

class SerializableOthrodromicPartitioner<T> extends
		OrthodromicDistancePartitioner<T> implements
		java.io.Serializable
{

	ConfigurationWrapper myConfiguration;

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
