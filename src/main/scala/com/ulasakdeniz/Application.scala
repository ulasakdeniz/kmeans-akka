package com.ulasakdeniz

import java.io.File

import akka.actor.{Props, ActorSystem}
import com.sksamuel.scrimage.Image
import com.typesafe.config.ConfigFactory
import com.ulasakdeniz.image.{ImageUtil, ImageWriter}
import com.ulasakdeniz.kmeans.{Start, Processor}

object Application extends App {

    val config = ConfigFactory.load("concurrency")
    val system = ActorSystem("actor-system", config)

    val projectDirectory = System.getProperty("user.dir")
    val imagePath = s"$projectDirectory/image/bird.jpg"
    val image = Image.fromFile(new File(imagePath))
    val imageDataList = ImageUtil.pixelDataList(image)

    val imageWriter = new ImageWriter(image.dimensions, s"$projectDirectory/image/bird-clustered.jpg")

    val k = 16

    val kMeansProcessor = system.actorOf(Props(classOf[Processor], imageDataList, k, 50, imageWriter), "processor")
    kMeansProcessor ! Start
}