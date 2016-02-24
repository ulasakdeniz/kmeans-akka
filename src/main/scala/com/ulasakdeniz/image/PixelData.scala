package com.ulasakdeniz.image

import com.sksamuel.scrimage.{Color, RGBColor}

case class PixelData(x: Int, y: Int, rgb: RGBColor)

object PixelData {

  def apply(x: Int, y: Int, argb: Int): PixelData = {
    PixelData(x, y, Color(argb))
  }

  implicit class RichPixel(rgb: RGBColor) {
    def distanceBetween(other: RGBColor): Int = {
      val sqr = (x: Int) => x * x
      sqr(rgb.red - other.red) + sqr(rgb.green - other.green) + sqr(rgb.blue - other.blue)
    }
  }
}