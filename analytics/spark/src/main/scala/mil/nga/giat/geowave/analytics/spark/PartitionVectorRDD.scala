package mil.nga.giat.geowave.analytics.spark

import org.apache.spark.SparkConf
import org.apache.spark.rdd.{ ShuffledRDD, RDD }
import mil.nga.giat.geowave.store.query.Query
import org.apache.hadoop.conf.Configuration
import org.opengis.feature.simple.SimpleFeature
import org.apache.spark.SparkContext
import org.apache.accumulo.core.data.{ Key, Value }
import mil.nga.giat.geowave.accumulo.mapreduce.input.GeoWaveInputFormat
import mil.nga.giat.geowave.accumulo.mapreduce.input.GeoWaveInputKey
import mil.nga.giat.geowave.analytics.tools.partitioners.Partitioner
import mil.nga.giat.geowave.analytics.tools.partitioners.Partitioner.PartitionData
import mil.nga.giat.geowave.index.ByteArrayId
import scala.collection.JavaConverters._

class PartitionVectorRDD(prev: RDD[(PartitionData, SimpleFeature)])
  extends ShuffledRDD[PartitionData, SimpleFeature, SimpleFeature](prev, new GeowavePartitioner(100))

object PartitionVectorRDD {

  def apply(prev: RDD[(GeoWaveInputKey, SimpleFeature)],
    partitioner: Partitioner[SimpleFeature]): PartitionVectorRDD = {

    val pointsKeyedByBoxes = prev.mapPartitions {
      it =>
        {
          for (p <- it; r <- partitioner.getCubeIdentifiers(p._2).asScala)
            yield (r, p._2)
        }
    }
    PartitionVectorRDD(pointsKeyedByBoxes)
  }

  def apply(pointsInBoxes: RDD[(PartitionData, SimpleFeature)]) = {
    new PartitionVectorRDD(pointsInBoxes)
  }

}