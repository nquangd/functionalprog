import patmat.Huffman._

val leaflist = List(Leaf('e', 2), Leaf('t', 3), Leaf('x', 4),Leaf('a', 5), Leaf('b', 7))


val a = List('a','b','c','d') zip List(1,2,3,4)

a(a.indexWhere(_._1 =='c'))._2

val c = List('a','b','b','c','a')


c.groupBy(identity).mapValues(_.size).toList
