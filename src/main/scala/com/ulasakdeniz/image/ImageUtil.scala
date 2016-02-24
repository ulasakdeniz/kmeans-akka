package com.ulasakdeniz.image

import com.sksamuel.scrimage.Image

object ImageUtil {

  def pixelDataList(image: Image): List[PixelData] = {
    val (width, height) = image.dimensions
    for {
      x <- (0 until width).toList
      y <- (0 until height).toList
    } yield PixelData(x, y, image.pixel(x, y).toColor)
  }
}