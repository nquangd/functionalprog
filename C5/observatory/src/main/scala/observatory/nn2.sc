import scala.math._

case class Tile(x: Int,y: Int, z: Short){
  def toLatLon = (
    toDegrees(atan(sinh(Pi * (1.0 - 2.0 * y.toDouble / (1<<z))))),
    x.toDouble / (1<<z) * 360.0 - 180.0,
    z)
  def toURI = new java.net.URI("https://tile.openstreetmap.org/"+z+"/"+x+"/"+y+".png")
}



Tile(1,0,1).toLatLon