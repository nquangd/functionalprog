

package observatory

import com.sksamuel.scrimage.{Image, Pixel}
import org.apache.log4j.{Level, Logger}

/**
  * 2nd milestone: basic visualization
  */
object Visualization {
import scala.math._
  import Extraction._
  /**
    * @param temperatures Known temperatures: pairs containing a location and the temperature at this location
    * @param location Location where to predict the temperature
    * @return The predicted temperature at `location`
    */

  def eq(x: Double, y: Double, precision: Double) = {
    if ((x - y).abs < precision) true else false
  }
  val pre = 1e-5

  def predictTemperature(temperatures: Iterable[(Location, Temperature)], location: Location): Temperature = {
    // Use inversed weighted interpolation
    val radius = 6378.137
    val p = 5
    def distance(x1:Location,x2:Location):Double = {

      if (eq(x1.lat,x2.lat,pre) && eq(x1.lon,x2.lon,pre)) 0d
      else if (eq(x1.lat+x2.lat,0d,pre) && (eq(x1.lon-x2.lon,180d,pre)|eq(x1.lon-x2.lon,-180d,pre))) radius*Pi
      else {

        val Δϕ = abs(x1.lat.toRadians - x2.lat.toRadians)
        val Δλ = abs(x1.lon.toRadians - x2.lon.toRadians)

        val a = pow(sin(Δϕ / 2), 2) + cos(x1.lat.toRadians) * cos(x2.lat.toRadians) * pow(sin(Δλ / 2), 2)
        radius * 2 * atan2(sqrt(a), sqrt(1 - a))
      }
      //radius * acos(sin(x1.lat.toRadians) * sin(x2.lat.toRadians) + cos(x1.lat.toRadians) * cos(x2.lat.toRadians) * cos(abs(x1.lon - x2.lon).toRadians))
    }
/*
    val delta = temperatures.par.map {
      case (otherLocation, temperature) => (distance(otherLocation,location),temperature)
    }

    val close : Option[(Double,Temperature)] = delta.find(_._1 < 1d)

    if (close.isDefined) {
      close.get._2
    }
    else {

      val weight = delta.par.map(l => ((1/pow(l._1,p))*l._2,1/pow(l._1,p)))
        .reduce((a,b)=> (a._1+b._1,a._2+b._2))

      weight._1/weight._2
    }

*/
    val delta = temperatures.map {    // par.map here would cause overhead cost in Manipulation modules
      case (otherLocation, temperature) => (distance(otherLocation,location),temperature)

   }
    val close : Option[(Double,Temperature)] = delta.find(_._1 < 1d)

    if (close.isDefined) {
      close.get._2
    }
    else {

        val weight = delta.aggregate((0.0, 0.0))(
          {
            case ((ws, iws), (distance, temp)) => {
              val w = 1 / pow(distance, p)
              (w * temp + ws, w + iws)
            }
          }, {
            case ((wsA, iwsA), (wsB, iwsB)) => (wsA + wsB, iwsA + iwsB)
          }
        )

        weight._1 / weight._2
      }

  }



  // Spark and dataframe for predicting temperature
 /*
 def sparkpred(temperatures: Iterable[(Location, Temperature)], location: Location): Temperature = {
    val radius = 6378.137
    val p = 2


    val distance = (x11:Double,x12:Double) => {
      val x1 = Location(x11,x12)
      if (eq(x1.lat,location.lat,pre) && eq(x1.lon,location.lon,pre)) 0d
      else if (eq(x1.lat+location.lat,0d,pre) && (eq(x1.lon-location.lon,180d,pre)|eq(x1.lon-location.lon,-180d,pre))) radius*Pi
      else
        radius * acos(sin(x1.lat) * sin(location.lat) + cos(x1.lat) * cos(location.lat) * cos(abs(x1.lon - location.lon)))
    }


    val rddtemp = Extraction.spark.sparkContext.parallelize(temperatures.toSeq,numSlices = 5)
    val scheme = StructType(Array(StructField("lat",DoubleType,nullable = false),
      StructField("lon",DoubleType,nullable = false),
      StructField("temp",DoubleType,nullable = false)))

    val rdd = rddtemp.map(x => Row(x._1.lat,x._1.lon,x._2))
    val dftemp = Extraction.spark.createDataFrame(rdd,scheme).cache()

      def findminmax(location: Location):List[(Double,Double)] = {
        val radius = 6378.137
        val maxdist = 1000 / radius // maxium search 200km
        val lat = location.lat* 2 * Pi / 360
        val lon = location.lon* 2 * Pi / 360
        val minlat = lat - maxdist
        val maxlat = lon + maxdist // latmin,latmax

        if (minlat > -Pi/2 && maxlat < Pi/2) {
          val deltalon = asin(sin(maxdist)/cos(lat))
          val mintemp = lon - deltalon
          val maxtemp = lon + deltalon
          val minlon = if (mintemp < -Pi) mintemp+2*Pi else mintemp
          val maxlon = if (maxtemp > Pi) maxtemp-2*Pi else maxtemp
          List((minlat*360/2/Pi,minlon*360/2/Pi),(maxlat*360/2/Pi,maxlon*360/2/Pi))
        }
        else {
          val latmin = max(minlat,-Pi/2)
          val latmax = min(maxlat,Pi/2)
          List((latmin*360/2/Pi,-180d),(latmax*360/2/Pi,180d))

        }

      }
     val range = findminmax(location)
    //println(location)
    //println(range)

    //val newdf = spark.sql("SELECT * FROM dftemp WHERE (lat >= range.head._1 AND lat <= range(2)._1) " +
    //  "AND (lon >= range.head._2 AND lon <= range(2)._2)")

    val df = dftemp.filter($"lat" >= range(0)._1  && $"lat" <= range(1)._1 && $"lon" >= range(0)._2 && $"lon" <= range(1)._2)
    .map(row => (distance(row(0).asInstanceOf[Double],row(1).asInstanceOf[Double]),Location(row(0).asInstanceOf[Double],row(1).asInstanceOf[Double]),row(2).asInstanceOf[Double]))
      .collect()

    //val delta = newtempfile.map(l => (distance(l._1,location),l._1,l._2))
    val close : Option[(Double,Location,Temperature)] = df.find(_._1 < 1d)

    if (close.isDefined) {
      close.get._3
    }
    else {
      val weight = df.map(l => ((1/pow(l._1,p))*l._3,1/pow(l._1,p)))
        .foldLeft((0d,0d)){ case ((accA,accB),(a,b))=> (accA+a,accB+b)}
      weight._1/weight._2
    }


  }

  */


  /**
    * @param points Pairs containing a value and its associated color
    * @param value The value to interpolate
    * @return The color that corresponds to `value`, according to the color scale defined by `points`
    */
  def interpolateColor(points: Iterable[(Temperature, Color)], value: Temperature): Color = {

    def diff(c1:Color,c2:Color):List[Int] = List(c1.red-c2.red,c1.green-c2.green,c1.blue-c2.blue)

    def plus(c1:Color,c2:Color):Color = Color(c1.red+c2.red,c1.green+c2.green,c1.blue+c2.blue)

    val pointsls = points.toList.sortBy(_._1).reverse

    val id = pointsls.map(x => x._1-value).indexWhere(p => p < 0d)

    if (id == 0) pointsls.head._2
    else if (id <0) pointsls.reverse.head._2
    else {

      val seg = (value - pointsls(id)._1) / (pointsls(id - 1)._1 - pointsls(id)._1)

      val col = diff(pointsls(id - 1)._2, pointsls(id)._2).map(x => (x.toDouble * seg).round.toInt)

      plus(pointsls(id)._2, Color(col.head, col(1), col(2)))
    }

  }

  /**
    * @param temperatures Known temperatures
    * @param colors Color scale
    * @return A 360×180 image where each pixel shows the predicted temperature at its location
    */
  Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
  def visualize(temperatures: Iterable[(Location, Temperature)], colors: Iterable[(Temperature, Color)]): Image = {

    def posToLocation(imageWidth: Int, imageHeight: Int)(pos: Int): Location = {
      val widthFactor = 180 * 2 / imageWidth.toDouble
      val heightFactor = 90 * 2 / imageHeight.toDouble

      val x: Int = pos % imageWidth
      val y: Int = pos / imageWidth

      Location(90 - (y * heightFactor), (x * widthFactor) - 180)
    }
    val imageWidth = 360
    val imageHeight = 180

    val locationMap = posToLocation(imageWidth, imageHeight) _

    val pixels = (0 until imageHeight * imageWidth).par.map {
      pos =>
        pos -> interpolateColor(
          colors,
          predictTemperature(
            temperatures,
            locationMap(pos)
          )
        ).pixel()
    }
      .seq
      .sortBy(_._1)
      .map(_._2)
/*
    val mx = (for {
      lat <- 90 to -89 by -1
      lon <- -180 to 179
    } yield Location(lat + 0.5, lon + 0.5)).toArray

    val pixels = mx.par.map{
      loc => interpolateColor(colors,predictTemperature(temperatures,loc)).pixel()
    }


*/
    Image(imageWidth,imageHeight,pixels.toArray)



  }

}

