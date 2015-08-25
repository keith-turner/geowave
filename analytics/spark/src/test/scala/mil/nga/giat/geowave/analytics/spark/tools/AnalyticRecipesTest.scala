package mil.nga.giat.geowave.analytics.spark.tools

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.scalatest.FlatSpec

import mil.nga.giat.geowave.adapter.vector.FeatureWritable
import mil.nga.giat.geowave.analytics.spark.GeoWaveRDD
import mil.nga.giat.geowave.analytics.spark.TestSuiteDataTools
import mil.nga.giat.geowave.analytics.spark.tools.AnalyticRecipes

class AnalyticRecipesTest extends FlatSpec {

  val dataTool = new TestSuiteDataTools("testRDD", "geometry:Geometry:srid=4326,pid:String")
  val dataSet = dataTool.create(1000)
  val conf = new SparkConf().setAppName(
    "AnalyticRecipesTest").setMaster(
      "local");
  GeoWaveRDD.init(conf)
  val sc = new SparkContext(conf)

  "One hundred distributed centroids" should "have 10 assigned items" in {
    val rawRDD = sc.parallelize(dataSet, 5)
    val distanceFn = dataTool.distanceFn

    val centroids = dataSet.sliding(1, 100).map(x => new FeatureWritable(x.last._2.getFeatureType(), x.last._2)).toArray
    
    val values = AnalyticRecipes.topK(rawRDD, distanceFn, centroids, 10);

    values.foreach(x => println(x._1 + "=" + x._2.length))

    assert(values.filter(_._2.length != 10).collect.size == 0)

  }
}