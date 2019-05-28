package observatory

import Extraction._
import java.io.File
import java.nio.file.Paths

import org.apache.spark.sql._
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._
import org.apache.spark.sql.expressions.scalalang.typed
import org.apache.log4j.{Level, Logger}


object Dtest  {
  import spark.implicits._

  import org.apache.spark.sql.types._
  import org.apache.spark.sql.SparkSession
  import org.apache.spark.sql.functions._
  import org.apache.spark.sql.functions.udf
  import java.time.LocalDate


  import org.apache.spark.sql.functions.udf
  import java.time.format.DateTimeFormatter
  import org.apache.spark.sql.Dataset

  def main(args: Array[String]): Unit = {
    //val path = getClass.getResourceAsStream("/1975.csv")
    /*
    val filelist:List[String] = {
      val path = getClass.getResource("/")
      println(path)
      val folder = new File(path.getPath)
      folder.listFiles
        .toList
        .map(file => file.getName).map(p => "/"+p)

      //if (folder.exists && folder.isDirectory)



    }*/
/*
    val year = 1997

    val id = filelist.indexWhere(x=>x.contains(year.toString))
    print(filelist(id))


    //filelist.map(x => if (x.contains(year.toString))  println(x))

    val loca = read("/stations.csv",1)

    val temp1997 = read("/1997.csv",0)



    val df1 = loca.filter($"lo" =!= 1000d)
    val df2 = temp1997

    //val res = df1.join(df2, df1("stn") <=> df2("stn") && df1("wban") <=> df2("wban"),"left")

    val res = df1.join(df2, Seq("wban","stn"))
    //val com=(s1:String,s2:String) => s1+s2+"1997"
    //val udfcom = udf(com)


    val com = (mon:String,day:String) => year.toString + mon + day
    val udfcom = udf(com)

    val newres = res.withColumn("year", udfcom(res.col("mon"),res.col("day")))
  newres.show()

    //res.filter($"temp" !== null).show()

    //val df = test._2
     // df.filter($"lo" =!= 0).show()
    //val arr = newres.rdd.map(row=>row.getString(0)).collect()

    //val ls = (newres.select($"lat",$"lo").as[Location].collect(),newres.select($"temp").as[Temperature].collect())

    val pat1 = DateTimeFormatter.ofPattern("yyyyMMdd")
    /*val ls = newres.map{row =>
      (LocalDate.parse(row(7).toString,pat1),Location(row(2).asInstanceOf[Double],row(3).asInstanceOf[Double]),row(6).asInstanceOf[Double])}
      .collect()*/

    val ls = newres.map{row =>
      (row(7).toString,Location(row(2).asInstanceOf[Double],row(3).asInstanceOf[Double]),row(6).asInstanceOf[Temperature])}
      .collect()
    println(ls(1))
*/
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    val test = locateTemperatures(1997,"/stations.csv","/1997.csv")

    val avg = locationYearlyAverageRecords(test)
    println(avg.toSeq(1))
    //val ii = test.groupBy(x => x._2).mapValues(_.map(_._3)).mapValues(z => z.sum/z.size).toSeq
   //println(ii(1))




    /*val df = sparkfunc(1997,"/stations.csv","/1997.csv").select("year","lat","lo","temp")


    df.groupBy("lat","lo").agg(avg("temp").as("averagetemp")).show()

  val sessionsDF = Seq(("day1","user1","session1", 100.0),("day1","user1","session2",200.0),
    ("day2","user1","session3",300.0),("day2","user1","session4",400.0),
    ("day2","user1","session4",99.0)) .toDF("day","userId","sessionId","purchaseTotal")

  val groupedSessions = sessionsDF.groupBy("day", "userId")
  groupedSessions.agg($"day", $"userId", countDistinct("sessionId"), sum("purchaseTotal")).show()

  */


    ////// Test for visualisation



  }
}

