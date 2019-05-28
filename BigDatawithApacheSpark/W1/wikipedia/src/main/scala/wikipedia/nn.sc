val x = List(("one","java jakarta"), ("two", "java indonesia"))
x.aggregate(0)((a,b)=> if (b._2.contains("java")) a+1 else a, _+_)


val y = List("java","C")

y map (a => (a,a.length))