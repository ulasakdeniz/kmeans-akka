package com.ulasakdeniz.kmeans

import akka.actor.Actor.emptyBehavior
import akka.event.LoggingReceive.withLabel
import akka.actor.{ActorLogging, Props, Actor}
import akka.routing.{FromConfig, Broadcast}
import com.sksamuel.scrimage.RGBColor
import com.ulasakdeniz.image.{Coordinate, ImageWriter, PixelData}

import scala.util.Random

class Processor(imageData: List[PixelData], k: Int, totalIteration: Int, imageWriter: ImageWriter)
  extends Actor with ActorLogging {

  val mapperRouter = context.actorOf(FromConfig.props(Props[MapperActor]), "mapperRouter")

  override def preStart() = {
    val initialMeans = Random.shuffle(imageData).take(k)
    val initialCentroids = initialMeans.map(pixelData => {
      val reducerRef = context.actorOf(Props[ReducerActor])
      val rgb = pixelData.rgb
      reducerRef ! rgb
      Centroid(rgb, reducerRef)
    })
    mapperRouter ! Broadcast(NewCentroids(initialCentroids))
    context become iterating(initialCentroids, imageData.iterator, 1)
  }

  def receive = emptyBehavior

  def waiting(counter: Int, centroids: List[Centroid], currentIteration: Int): Receive = withLabel("waiting"){
    case rgb: RGBColor => {
      val newCentroid = Centroid(rgb, sender)
      sender ! PrepareForNextIteration
      if(counter == k) {
        val newIterator = imageData.iterator
        context become iterating(newCentroid :: centroids, newIterator, currentIteration + 1)
        mapperRouter ! Broadcast(Start)
      }
      else {
        context become waiting(counter + 1, newCentroid :: centroids, currentIteration)
      }
    }
  }

  def iterating(centroids: List[Centroid], iterator: Iterator[PixelData], currentIteration: Int): Receive = withLabel("iterating"){
    case Start => {
      mapperRouter ! Broadcast(Start)
    }
    case NextJob => {
      if(iterator.hasNext) {
        mapperRouter ! iterator.next
      }
      else {
        if(currentIteration != totalIteration) {
          centroids.foreach(centroid => centroid.reducerRef ! EndOfIteration)
          context become waiting(1, List.empty, currentIteration)
        }
        else {
          centroids.foreach(centroid => centroid.reducerRef ! SendClusterData)
          context become printing(0, Map.empty)
        }
      }
    }
  }

  def printing(counter: Int, clusterMap: Map[RGBColor, List[Coordinate]]): Receive = withLabel("printing"){
    case ClusterData(currentCentroid, points) => {
      if(counter + 1 == k) {
        imageWriter.write(clusterMap + (currentCentroid -> points))
        context.system.terminate()
      }
      else {
        context become printing(counter + 1, clusterMap + (currentCentroid -> points))
      }
    }
  }
}