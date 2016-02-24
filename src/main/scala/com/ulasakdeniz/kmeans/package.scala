package com.ulasakdeniz

import akka.actor.ActorRef
import com.sksamuel.scrimage.RGBColor
import com.ulasakdeniz.image._

package object kmeans {

  case class NewCentroids(centroids: List[Centroid])
  case class Centroid(data: RGBColor, reducerRef: ActorRef)

  case object Start
  case object NextJob

  case class ClusterData(centroidRGB: RGBColor, clusterPoints: List[Coordinate])
  case object EndOfIteration
  case object PrepareForNextIteration
  case object SendClusterData
}
