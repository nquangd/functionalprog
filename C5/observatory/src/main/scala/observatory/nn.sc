
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.io.File
import scala.math

val pat1 = DateTimeFormatter.ofPattern("yyyyMMdd")

val date = "20150128"

LocalDate.parse(date,pat1)

LocalDate.of(2018,3,29)

val path = getClass.getResourceAsStream("/1975.csv")


val l = List("+100","","-100","")

l.take(2) ::: l.drop(2)

val colnames = List(List("stn","wban","mon","day","temp"),List("stn","wban","lat","lo"))

colnames(1)

val com = (str:String,str2:String) => str+str2+"1997"
com("3","4")


val ls = List((1,2),(4,0),(3,1))

ls.sortBy(_._1).reverse


234.8.round.toInt


val ls2 = List(1,2,3,-1,-2,-3)

ls2.indexWhere(p => p>0)

 1 + 0.5

val pxfrid = for {
 lon <- -180 to 179      // 360px
 lat <- 90 to -89 by -1   //180 px
} yield (lon+0.5,lat-0.5)

266039206145d / 1e9
//ls.aggregate((0,0))((a,b)=>(a._1+b._1,a._2+b._2),_+_)

val x = 0
val y = 0

(1 % 256).toDouble / 256 + 0

