import patmat.Huffman._

val leaflist = List(Leaf('e', 2), Leaf('t', 3), Leaf('x', 4),Leaf('a', 5), Leaf('b', 7))


val b = combine(leaflist)

val d =combine(b)

combine(d).length


val a = List(1)
a.head
val f: List[CodeTree]=>Boolean = singleton
val ff:  List[CodeTree] => List[CodeTree]= combine
//until(f,ff)(leaflist)
until(singleton(_:List[CodeTree]),combine(_:List[CodeTree]))(leaflist)




