def pair(x: Char, y:Int): (Char, Int) = (x.toChar,y)
val a = List('a','b') zip List(1,2)
val b = List(("Mercury", 57.9), ("Venus", 108.2), ("Earth", 149.6),
  ("Mars", 227.9), ("Jupiter", 778.3))
val c = b.find(_._1=="Mercury").map{case (c,cc)=>(c,1)}.toList

c:::b

b.find(_._1=="Mercury")
b.filter(_._1=="Mercury2").isEmpty
b.count(_._1=="Mercury")
a.find(_._1=='a')

b.exists(_._1=="Mercury2")

val c = List('a,'b')

