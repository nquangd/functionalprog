List(('a', 2))

import forcomp.Anagrams._




val s = combinations(List(('b', 2), ('a', 2)))

(s map (x=> x.sortBy(_._1)))

val x = List(('a', 1), ('d', 1), ('l', 1), ('r', 1))
val y = List(('r', 1))

val tt = x:::y





subtract(x,y)






val ss = tt.groupBy(y => y._1).mapValues{
  case head::Nil => head._2
  case _ =>  x(1)._2 - x(1)._2
}.toList. filterNot(x=>x._2==0). sortBy(x=>x._1)




sentenceAnagrams(List("ate"))