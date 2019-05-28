package observatory

import com.sksamuel.scrimage.{Image, Pixel}
import Visualization._

/**
  * 3rd milestone: interactive visualization
  */
object Interaction {

  /**
    * @param tile Tile coordinates
    * @return The latitude and longitude of the top-left corner of the tile, as per http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
    */
  def tileLocation(tile: Tile): Location = {
    tile.tolocation
  }

  /**
    * @param temperatures Known temperatures
    * @param colors Color scale
    * @param tile Tile coordinates
    * @return A 256×256 image showing the contents of the given tile
    */
  def tile(temperatures: Iterable[(Location, Temperature)], colors: Iterable[(Temperature, Color)], tile: Tile): Image = {
    // tile return the location at the topleft corner
    // pixel.location + tile location => pixel.real location
    val imageWidth = 256
    val imageHeight = 256
    val tileloc = tile.tolocation

    val pixels = (0 until imageHeight*imageWidth)
      .par
      .map{ pos =>
        val subx = (pos % imageWidth) + tile.x * imageWidth
        val suby = (pos / imageHeight) + tile.y * imageHeight
        val subpos = Tile(subx,suby,tile.zoom + 8).tolocation

        //val pixelpos = Location(tileloc.lat + subpos.lat, tileloc.lon + subpos.lon)
        pos -> interpolateColor(colors,
          predictTemperature(temperatures,subpos))
          .pixel(127)
      }.seq
      .sortBy(_._1)
      .map(_._2)

    Image(imageWidth,imageHeight,pixels.toArray)

  }

  /**
    * Generates all the tiles for zoom levels 0 to 3 (included), for all the given years.
    * @param yearlyData Sequence of (year, data), where `data` is some data associated with
    *                   `year`. The type of `data` can be anything.
    * @param generateImage Function that generates an image given a year, a zoom level, the x and
    *                      y coordinates of the tile and the data to build the image from
    */
  def generateTiles[Data](
    yearlyData: Iterable[(Year, Data)],
    generateImage: (Year, Tile, Data) => Unit
  ): Unit = {
    for {
      (year, data) <- yearlyData
      zoom <- 0 to 3
      x <- 0 until 1 << zoom
      y <- 0 until 1 << zoom
  } {
      generateImage(year, Tile(x, y, zoom),data)
    }
    }


}
