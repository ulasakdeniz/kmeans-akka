k-means Image Clustering with Akka
==================================

A k-means implementation that clusters RGB values of a given image and then writes the new values to an image file.

## Implementation

At first, all the RGB values are sent to `Processor` actor to start clustering. `Processor` selects `k` centroids randomly and spawns a `ReducerActor` for each. `Processor` sends RGBs to `mapperRouter` which is a round-robin router of `MapperActor`s. Each `MapperActor` finds the closest centroid and send RGB data to associated `ReducerActor`. Once all the data are processed, the first iteration finishes. This process continues until the `totalIteration` is reached. At last, a new image is created according to final centroid values.

Original Image                       |  Clustered Image
:-----------------------------------:|:----------------------------------------------:
![Original Image](image/wall-e.png)  |  ![Clustered Image](image/wall-e-clustered.png)

## Usage

There is an example image in `image` directory. It is taken from the movie `WALL-E`. By default, `image` is the input/output directory of the program. You can change it by specifying a different `imagePath` in Application object. To run the program type: `sbt run`
