package mil.nga.giat.geowave.analytics.spark

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.hadoop.conf.Configuration
import org.opengis.feature.simple.SimpleFeature
import org.apache.spark.SparkContext
import org.apache.accumulo.core.data.{ Key, Value }
import mil.nga.giat.geowave.datastore.accumulo.mapreduce.input.GeoWaveInputKey
import mil.nga.giat.geowave.core.store.query.Query
import mil.nga.giat.geowave.datastore.accumulo.mapreduce.input.GeoWaveInputFormat
import mil.nga.giat.geowave.analytic.ConfigurationWrapper
import org.apache.spark.serializer.KryoRegistrator
import com.esotericsoftware.kryo.Kryo

object GeoWaveRDD {

  def init(conf: SparkConf): SparkConf = {
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");

    conf.set(
      "spark.serializer",
      "org.apache.spark.serializer.KryoSerializer");
    conf.set(
      "spark.kryo.registrator",
      classOf[GeoWaveKryoRegistrator].getCanonicalName());
  }

  def featureRDD(conf: Configuration, sc: SparkContext,
    query: Query): RDD[(GeoWaveInputKey, SimpleFeature)] = {
    sc.newAPIHadoopRDD(conf, classOf[GeoWaveInputFormat[SimpleFeature]], classOf[GeoWaveInputKey], classOf[SimpleFeature])
  }

  def neighborPartition(rdd: RDD[(GeoWaveInputKey, SimpleFeature)], config: ConfigurationWrapper): PartitionVectorRDD = {
    val distancePartitioner = new SerializableOthrodromicPartitioner[SimpleFeature]();
    distancePartitioner.initialize(config);
    PartitionVectorRDD(rdd, distancePartitioner)
  }
}