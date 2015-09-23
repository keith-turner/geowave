package mil.nga.giat.geowave.analytics.spark.tools

import scala.collection.mutable.PriorityQueue
import org.opengis.feature.simple.SimpleFeature
import mil.nga.giat.geowave.analytic.distance.DistanceFn
import mil.nga.giat.geowave.datastore.accumulo.mapreduce.input.GeoWaveInputKey
import org.apache.spark.rdd.RDD
import mil.nga.giat.geowave.adapter.vector.FeatureWritable
import org.apache.spark.SparkContext._

object AnalyticRecipes {

  def findClosest(key: GeoWaveInputKey, feature: SimpleFeature, distFn: DistanceFn[SimpleFeature], centroids: List[SimpleFeature]) = {
    val best = centroids.map(c => (c, distFn.measure(feature, c))).sortBy(n => n._2).head
    (best._1.getID(), (best._2, feature))
  }
  
  def distanceToAll(key: GeoWaveInputKey, feature: SimpleFeature, distFn: DistanceFn[SimpleFeature], centroids: List[SimpleFeature]) = {
     
  }

  /**
   * Centroids are of type FeatureWritable for serializable.
   * 
   * Given centroids, find the k closest distinct neighbors to each centroid.
   * 
   * What does 'distinct' mean in this case? This algorithm does not return the k closest neighbors for any given centroid.
   * Rather it allows each input vector to choose the closest centroid, retaining the k closest to the centroid.  The distinction
   * lies with who does the choosing.  If the centroid chooses, then any given input vector could be a close neighor to more than
   * one centroid.     
   * 
   */
  def topDistinctK(rdd: RDD[(GeoWaveInputKey, SimpleFeature)], distanceFn: DistanceFn[SimpleFeature], centroids: Array[FeatureWritable], k: Int) = {
    val associationsToCentroidsRDD = rdd.map(x => { findClosest(x._1, x._2, distanceFn, centroids.map(fw => fw.getFeature).toList) })
    val addToSet = (s: Array[(Double, SimpleFeature)], v: (Double, SimpleFeature)) => { AnalyticRecipes.addToTopN(k, v, s) }
    val mergePartitionSets = (p1: Array[(Double, SimpleFeature)], p2: Array[(Double, SimpleFeature)]) => {
      p1.foldLeft(p2)((s1, v) => AnalyticRecipes.addToTopN(k, v, s1))
    }
    associationsToCentroidsRDD.aggregateByKey(new Array[(Double, SimpleFeature)](10))(addToSet, mergePartitionSets)
  }

  private def addToTopN(n: Int, newEl: (Double, SimpleFeature), list: Array[(Double, SimpleFeature)]): Array[(Double, SimpleFeature)] = {

    def addToList(sofar: List[(Double, SimpleFeature)], el: (Double, SimpleFeature)): List[(Double, SimpleFeature)] = sofar match {
      case List() => List(el)
      case head :: tail => if (head._1 < el._1)
        head :: addToList(tail, el)
      else
        el :: head :: tail
    }

    val initialList = list.toList.filter(_ != null);
    val r = addToList(initialList, newEl)
    (if (r.size > n) r.dropRight(r.size - n) else r).toArray
  }

  val ordering = new Ordering[(Double, SimpleFeature)]() {
    def compare(lt: (Double, SimpleFeature), rt: (Double, SimpleFeature)): Int = {
      if ((lt._1 - rt._1) < 0) -1 else 1;
    }
  }

  /**
   * Will only work in Scala 2.11.2
   */
  private def addToTopN(n: Int, newEl: (Double, SimpleFeature), list: PriorityQueue[(Double, SimpleFeature)]): PriorityQueue[(Double, SimpleFeature)] = {
    list.enqueue(newEl);
    if (list.size > n) list.take(n) else list
  }
  
  
  /**
   * Centroids are of type FeatureWritable for serializable.
   * 
   * Given centroids, find the k closest neighbors to each centroid.
   * 
   * The centroid chooses, thus any given input vector could be a close neighor to more than
   * one centroid.     
   * 
   */
  def topK(rdd: RDD[(GeoWaveInputKey, SimpleFeature)], distanceFn: DistanceFn[SimpleFeature], centroids: Array[FeatureWritable], k: Int) = {
    val associationsToCentroidsRDD = rdd.flatMap(x => centroids.map(fw => fw.getFeature).toList.map(c => (c, (distanceFn.measure(x._2, c),x._2))))
    val addToSet = (s: Array[(Double, SimpleFeature)], v: (Double, SimpleFeature)) => { AnalyticRecipes.addToTopN(k, v, s) }
    val mergePartitionSets = (p1: Array[(Double, SimpleFeature)], p2: Array[(Double, SimpleFeature)]) => {
      p1.foldLeft(p2)((s1, v) => AnalyticRecipes.addToTopN(k, v, s1))
    }
    associationsToCentroidsRDD.aggregateByKey(new Array[(Double, SimpleFeature)](10))(addToSet, mergePartitionSets)
  }

}