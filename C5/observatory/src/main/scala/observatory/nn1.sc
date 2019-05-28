import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import java.nio.file.Paths
import java.io._

import observatory.Extraction.getClass
import org.apache.spark.metrics.source

def fsPath(resource: String): String =
  Paths.get(getClass.getResource(resource).toURI).toString

val file = getClass.getResourceAsStream("/1997.csv")
val input = Source.fromFile(fsPath("/1997.csv"))
case class TempFile(stn: String, wban: String, mon: Int, day: Int, temp: Double)

val out  = for {
  line <- input.getLines()

  value = line.split(",",-1)
} yield (value(0),value(1),value(2),value(3),value(4))


println(out.toList.length)
