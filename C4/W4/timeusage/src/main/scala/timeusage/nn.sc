import timeusage.TimeUsage._


val name = List("tucaseid","t1803010101","t050102","t010199","t020201","t010299","t010301","t010399","t010401","t010499","t010501")
val cat = List(
  List("t01", "t03", "t11", "t1801", "t1803"),
  List("t05", "t1805"),
  List("t02", "t04", "t06", "t07", "t08", "t09", "t10", "t12", "t13", "t14", "t15", "t16", "t18")
).zipWithIndex

  val ll:Map[Int,List[String]] = name flatMap { str:String => cat
    .flatMap(ls =>
    if (ls._1.exists(str.startsWith)) Some((ls._2,str)) else None)
    .sortBy(_._1)
    .headOption  match {
    case None => None
    case Some(turple)=> Some(turple)
  }

  }

    //.groupBy(x => x._1).mapValues(_.map(_._2))
