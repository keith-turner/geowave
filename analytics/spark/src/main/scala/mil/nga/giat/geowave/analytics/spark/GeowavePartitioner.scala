package mil.nga.giat.geowave.analytics.spark

import org.apache.spark.rdd.RDD
import mil.nga.giat.geowave.index.ByteArrayId
import mil.nga.giat.geowave.analytics.tools.partitioners.Partitioner.PartitionData

class GeowavePartitioner(private val size: Int)
  extends org.apache.spark.Partitioner
  with Serializable {

  override def numPartitions: Int = size

  override def getPartition(key: Any): Int = {
    key match {
      case (b1: ByteArrayId) => b1.hashCode % size
      case (pd: PartitionData) => pd.id.hashCode % size
      case _ => 0 // Throw an exception?
    }
  }

}