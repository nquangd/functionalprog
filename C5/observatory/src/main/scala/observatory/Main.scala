package observatory

import System.nanoTime

object Main extends App {

  import Extraction._

  def profile[R](code: => R, t: Long = nanoTime) = (code, nanoTime - t)
  //val (result, time) = profile{
  val temp = locateTemperatures(1997,"/stations.csv","/1997.csv")
  val avg = locationYearlyAverageRecords(temp).toSeq

    println(temp.size)
    println(avg.size)
 // }
  //println(time.toDouble/1e9)



  val sample= Iterable((60d,Color(255,255,255)),
    (32d,Color(255,0,0)),
    (12d,Color(255,255,0)),
    (0d,	Color(0,255,255)),
    (-15d,	Color(0,0,255)),
    (-27d,	Color(255,0,255)),
    (-50d,	Color(33,0,107)),
    (-60d,	Color(0,0,0)))

// Milestone 2

  import Visualization._



val (result, time) = profile{
      val myImage = visualize(avg, sample)
  myImage.output(new java.io.File("./some-image.png"))

  }
  println(time.toDouble/1e9)



  // Milestone 3
/*
  import Interaction._
  for (x <- 0 to 1; y <- 0 to 1) {
    val tile1 = Tile(x, y, 1)
    val imageMilestone3 = tile(avg, sample, tile1)
    imageMilestone3.output(new java.io.File("./" + x.toString + "-" + y.toString + ".png"))
  }
*/
/*
  import Manipulation._

  val year1 = avg.take(50)
  val year2 = avg.slice(50,100)
  val year = Iterable(year1,year2)
  val avggrid = year.map(makeGrid)

val test1 = avggrid.map(x => x(GridLocation(0,0)))
  println(test1)

  */
}
