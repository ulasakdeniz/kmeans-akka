package com.ulasakdeniz.kmeans

import akka.actor.Actor
import com.ulasakdeniz.image.PixelData

class MapperActor extends Actor {

  override def receive = idle

  def idle: Receive = {
    case NewCentroids(centroids) => {
      context become working(centroids)
    }
  }

  def working(centroids: List[Centroid]): Receive = {
    case Start => {
      sender ! NextJob
    }
    case p@PixelData(_, _, rgb) => {
      import com.ulasakdeniz.image.PixelData._
      //find the closest centroid and send pixelData to associated ReducerActor
      val closestCentroid = centroids.minBy(c => c.data.distanceBetween(rgb))
      closestCentroid.reducerRef ! p
      sender ! NextJob
    }
  }
}