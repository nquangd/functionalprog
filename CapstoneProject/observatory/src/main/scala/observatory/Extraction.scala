package observatory

import java.time.LocalDate
import java.nio.file.Paths

import org.apache.spark.sql._
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._
import java.io.File
import java.time.format.DateTimeFormatter

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
/**
  * 1st milestone: data extraction
  */
object Extraction {

  /**
    * @param year             Year number
    * @param stationsFile     Path of the stations resource file to use (e.g. "/stations.csv")
    * @param temperaturesFile Path of the temperatures resource file to use (e.g. "/1975.csv")
    * @return A sequence containing triplets (date, location, temperature)
    */

  Logger.getLogger("org.apache.spark").setLevel(Level.WARN)

  val spark: SparkSession =
    SparkSession
      .builder()
      .appName("Capstone")
      .config("spark.master", "local")
      .getOrCreate()

  import spark.implicits._

  val filelist:List[String] = {
    val path = getClass.getResource("/")
    val folder = new File(path.getPath)
      folder.listFiles
        .toList
        .map(file => file.getName).map(p => "/"+p)
  }

  val colnames = List(List("stn","wban","mon","day","temp"),List("stn","wban","lat","lon"))

  def dfSchema(columnNames: List[String], filetype:Int): StructType = {
    if (filetype == 0) {
    StructType(columnNames.take(2).map(fieldname => StructField(fieldname, StringType, nullable = true))
      ::: columnNames.slice(2, 4).map(fieldname => StructField(fieldname, IntegerType, nullable = false))
      ::: columnNames.drop(4).map(fieldname =>StructField(fieldname, DoubleType, nullable = false)))
  }
    else {
      StructType(columnNames.take(2).map(fieldname => StructField(fieldname, StringType, nullable = true))
        ::: columnNames.drop(2).map(fieldname => StructField(fieldname, DoubleType, nullable = false)))
    }
  }
  def convertodouble(s: String):Option[Double] = {
    try {
      Some(s.toDouble)
    } catch {
      case e: NumberFormatException => None
    }
  }
  def row(line: List[String], filetype:Int): Row = {
    if (filetype == 0) Row(line.take(2).map(_.toString) :::line.slice(2, 4).map(_.toInt) ::: line.drop(4).map(_.toDouble): _*)
    //else Row(line.take(2).map(_.toString) ::: line.drop(2).map(_.toDouble): _*)

    else Row(line.take(2).map(_.toString) ::: line.drop(2).map(str => convertodouble(str).getOrElse(1000d)): _*)
  }
  def fsPath(resource: String): String =
    Paths.get(getClass.getResource(resource).toURI).toString


  def read(resource: String, filetype:Int): DataFrame = {

    val rdd = spark.sparkContext.textFile(fsPath(resource))
    val schema = dfSchema(colnames(filetype),filetype)
    val data =
      rdd.map(_.split(",",-1).to[List])
        .map(r => row(r,filetype))
    val dataFrame =
      spark.createDataFrame(data, schema)

    dataFrame


/*
    val scheme = dfSchema(colnames(filetype),filetype)
    spark.sqlContext.read.format("csv")
      .option("header","false")
      .schema(scheme)
      .load(fsPath(resource))

*/
  }


  def sparkfunc(year: Year, stationsFile: String, temperaturesFile: String): Dataset[TempReduce] = {
    val loca = read(stationsFile,1).where($"lon" =!= 1000d) //.as[Stations]
    val temp = read(temperaturesFile,0) //.as[TempFile]

    val join = loca.join(temp, Seq("stn","wban"))


    //join.show()
       join
      .as[StationTemp]
      .map(r => (Date(year,r.mon,r.day),Location(r.lat,r.lon),r.temp))
      .toDF("date","location","temp")
      .as[TempReduce]



    //val com = (mon:String,day:String) => year.toString + mon + day
    //val date = udf(com)
    //df.withColumn("year", date(df.col("mon"),df.col("day"))). select("year","lat","lon","temp")
      //df.select("mon","day","lat","lon","temp")

    //join.select("mon","day","lat","lon","temp")

  }

  def locateTemperatures(year: Year, stationsFile: String, temperaturesFile: String): Iterable[(LocalDate, Location, Temperature)] = {

    val res = sparkfunc(year,stationsFile,temperaturesFile)

    //val pat1 = DateTimeFormatter.ofPattern("yyyyMMdd")

    res.collect()
      .par
      .map(r => (r.date.tolocaldate,r.location,(r.temp-32)*5/9))
      .seq



    /*
    res.map{row =>
      (row(0).asInstanceOf[Int], row(1).asInstanceOf[Int],Location(row(2).asInstanceOf[Double],row(3).asInstanceOf[Double]),(row(4).asInstanceOf[Temperature]-32)*5/9)}
      //(row(0).toString,Location(row(1).asInstanceOf[Double],row(2).asInstanceOf[Double]),(row(3).asInstanceOf[Temperature]-32)*5/9)}
      .collect()
      .par
      .map(tp => (LocalDate.of(year, tp._1,tp._2),tp._3,tp._4))
      .seq
*/



  }

  /**
    * @param records A sequence containing triplets (date, location, temperature)
    * @return A sequence containing, for each location, the average temperature over the year.
    */
  def locationYearlyAverageRecords(records: Iterable[(LocalDate, Location, Temperature)]): Iterable[(Location, Temperature)] = {

    records
      .par
      .groupBy(_._2)
      .mapValues(
        l => l.foldLeft(0.0)((t,r) => t + r._3) / l.size
      )
      .seq

  }

}
