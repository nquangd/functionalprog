
val a = "aabbccccdd".toList
a.sorted


def pair(x: Char, y:Int): (Char, Int) = (x.toChar,y)
List(pair('a',2))



def times(chars: List[Char]): List[(Char, Int)] =  {
  def freq(chars:List[Char], xs: List[(Char, Int)]):List[(Char, Int)] = chars match {
    case Nil => xs
    case head::tail => {
      val count = if (xs.exists(_._1 == head)) xs.find(_._1 == head).map{case (c,cc)=>(c,cc+1)}.toList
      else List(head) zip List(1)
      freq(tail,count ::: xs.filterNot(_._1 == head))
    }
  }
  freq(chars,List())

}
times(a)

val b = List(1,1)::2::3::Nil
val c = times(a)

c.sortBy(_._2).reverse
c.head._2

abstract class CodeTree
case class Fork(left: CodeTree, right: CodeTree, chars: List[Char], weight: Int) extends CodeTree
case class Leaf(char: Char, weight: Int) extends CodeTree

def makeOrderedLeafList(freqs: List[(Char, Int)]): List[Leaf] = {

  val a = freqs.sortBy(_._2).reverse
  if (freqs.isEmpty) List() else
  Leaf(a.head._1,a.head._2)::makeOrderedLeafList(a.tail)

}

makeOrderedLeafList(times(a))