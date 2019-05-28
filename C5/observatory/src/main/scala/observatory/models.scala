package observatory

import com.sksamuel.scrimage.RGBColor
import java.time.LocalDate
import scala.math._
/**
  * Introduced in Week 1. Represents a location on the globe.
  * @param lat Degrees of latitude, -90 ≤ lat ≤ 90
  * @param lon Degrees of longitude, -180 ≤ lon ≤ 180
  */
case class Location(lat: Double, lon: Double)
/**
  * Introduced in Week 3. Represents a tiled web map tile.
  * See https://en.wikipedia.org/wiki/Tiled_web_map
  * Based on http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
  * @param x X coordinate of the tile
  * @param y Y coordinate of the tile
  * @param zoom Zoom level, 0 ≤ zoom ≤ 19
  */
case class Tile(x: Int, y: Int, zoom: Int) {
  lazy val tolocation: Location = Location(
    toDegrees(atan(sinh(Pi * (1.0 - 2.0 * y.toDouble / (1 << zoom))))),
    x.toDouble / (1 << zoom) * 360.0 - 180.0)
}

/**
  * Introduced in Week 4. Represents a point on a grid composed of
  * circles of latitudes and lines of longitude.
  * @param lat Circle of latitude in degrees, -89 ≤ lat ≤ 90
  * @param lon Line of longitude in degrees, -180 ≤ lon ≤ 179
  */
case class GridLocation(lat: Int, lon: Int)

/**
  * Introduced in Week 5. Represents a point inside of a grid cell.
  * @param x X coordinate inside the cell, 0 ≤ x ≤ 1
  * @param y Y coordinate inside the cell, 0 ≤ y ≤ 1
  */
case class CellPoint(x: Double, y: Double)

/**
  * Introduced in Week 2. Represents an RGB color.
  * @param red Level of red, 0 ≤ red ≤ 255
  * @param green Level of green, 0 ≤ green ≤ 255
  * @param blue Level of blue, 0 ≤ blue ≤ 255
  */
case class Color(red: Int, green: Int, blue: Int) {
  def pixel(alpha: Int = 255) = RGBColor(red, green, blue, alpha).toPixel
}

case class Stations(stn: String, wban: String, lat: Double, lon: Double)
case class TempFile(stn: String, wban: String, mon: Int, day: Int, temp: Double)
case class StationTemp(stn: String, wban: String, lat: Double, lon: Double, mon: Int, day: Int, temp: Double)

//case class StationTemp(stn: String, lat: Double, lon: Double, mon: Int, day: Int, temp: Double)

case class Date(year: Int, mon: Int, day: Int) {
  def tolocaldate = LocalDate.of(year,mon,day)
}
case class TempReduce(date: Date, location: Location, temp: Temperature)


class temparray {
  private var temps: Array[Temperature] = new Array[Temperature](360*180)

  private def toindex(lat: Int, lon: Int): Int = {
    val x = lon + 180
    val y = lat + 89
    y * 360 + x
  }
  def insert(lat: Int, lon: Int, temp: Temperature): Unit = {
    temps(toindex(lat, lon)) = temp
  }

  def retrive(lat: Int, lon: Int): Temperature = {
    temps(toindex(lat, lon))
  }

  def calculate(temps: Iterable[(Location, Temperature)]): Unit = {
    for {
      lat <- Range(90, -90, -1)
      lon <- -180 until 180
    } insert(lat, lon, Visualization.predictTemperature(temps, Location(lat, lon)))
  }
  def +=(that: temparray): temparray = {
    temps.indices.foreach(idx => this.temps(idx) += that.temps(idx))
    this
  }

  def /=(denominator: Double): temparray = {
    temps = temps.map(_ / denominator)
    this
  }

  def -=(that: GridLocation => Temperature): temparray = {
    for {
      lat <- Range(90, -90, -1)
      lon <- -180 until 180
    } insert(lat, lon, retrive(lat, lon) - that(GridLocation(lat, lon)))
    this
  }


}