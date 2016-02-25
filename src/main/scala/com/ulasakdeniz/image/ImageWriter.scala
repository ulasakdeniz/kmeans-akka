package com.ulasakdeniz.image

import com.sksamuel.scrimage.nio.PngWriter
import com.sksamuel.scrimage.{Image, RGBColor}

class ImageWriter(dimensions: (Int, Int), pathToWrite: String) {

  def write(clusterMap: Map[RGBColor, List[Coordinate]]) = {
    val image = Image.apply(dimensions._1, dimensions._2)

    clusterMap.foreach(p => {
      p._2.foreach(coord => image.setPixel(coord._1, coord._2, p._1.toPixel))
    })

    implicit val writer = PngWriter.NoCompression
    image.output(pathToWrite)
  }
}