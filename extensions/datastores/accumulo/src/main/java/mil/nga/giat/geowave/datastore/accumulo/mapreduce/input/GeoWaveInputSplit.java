package mil.nga.giat.geowave.datastore.accumulo.mapreduce.input;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import mil.nga.giat.geowave.core.index.PersistenceUtils;
import mil.nga.giat.geowave.core.store.index.Index;

import org.apache.accumulo.core.data.Range;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputSplit;

/**
 * The Class GeoWaveInputSplit. Encapsulates a GeoWave Index and a set of
 * Accumulo ranges for use in Map Reduce jobs.
 */
public class GeoWaveInputSplit extends
		InputSplit implements
		Writable
{
	private Map<Index, List<RangeLocationPair>> ranges;
	private String[] locations;

	protected GeoWaveInputSplit() {
		ranges = new HashMap<Index, List<RangeLocationPair>>();
		locations = new String[] {};
	}

	protected GeoWaveInputSplit(
			final Map<Index, List<RangeLocationPair>> ranges,
			final String[] locations ) {
		this.ranges = ranges;
		this.locations = locations;
	}

	public Set<Index> getIndices() {
		return ranges.keySet();
	}

	public List<RangeLocationPair> getRanges(
			final Index index ) {
		return ranges.get(index);
	}

	/**
	 * This implementation of length is only an estimate, it does not provide
	 * exact values. Do not have your code rely on this return value.
	 */
	@Override
	public long getLength()
			throws IOException {
		long diff = 0;
		for (final Entry<Index, List<RangeLocationPair>> indexEntry : ranges.entrySet()) {
			for (final RangeLocationPair range : indexEntry.getValue()) {
				diff += (long) range.getCardinality();
			}
		}
		return diff;
	}

	@Override
	public String[] getLocations()
			throws IOException {
		return locations;
	}

	@Override
	public void readFields(
			final DataInput in )
			throws IOException {
		final int numIndices = in.readInt();
		ranges = new HashMap<Index, List<RangeLocationPair>>(
				numIndices);
		for (int i = 0; i < numIndices; i++) {
			final int indexLength = in.readInt();
			final byte[] indexBytes = new byte[indexLength];
			in.readFully(indexBytes);
			final Index index = PersistenceUtils.fromBinary(
					indexBytes,
					Index.class);
			final int numRanges = in.readInt();
			final List<RangeLocationPair> rangeList = new ArrayList<RangeLocationPair>(
					numRanges);

			for (int j = 0; j < numRanges; j++) {
				try {
					final RangeLocationPair range = new RangeLocationPair();
					range.readFields(in);
					rangeList.add(range);
				}
				catch (InstantiationException | IllegalAccessException e) {
					throw new IOException(
							"Unable to instantiate range",
							e);
				}
			}
			ranges.put(
					index,
					rangeList);
		}
		final int numLocs = in.readInt();
		locations = new String[numLocs];
		for (int i = 0; i < numLocs; ++i) {
			locations[i] = in.readUTF();
		}
	}

	@Override
	public void write(
			final DataOutput out )
			throws IOException {
		out.writeInt(ranges.size());
		for (final Entry<Index, List<RangeLocationPair>> range : ranges.entrySet()) {
			final byte[] indexBytes = PersistenceUtils.toBinary(range.getKey());
			out.writeInt(indexBytes.length);
			out.write(indexBytes);
			final List<RangeLocationPair> rangeList = range.getValue();
			out.writeInt(rangeList.size());
			for (final RangeLocationPair r : rangeList) {
				r.write(out);
			}
		}
		out.writeInt(locations.length);
		for (final String location : locations) {
			out.writeUTF(location);
		}
	}
}
