import patmat.Huffman._

val leaflist = List(Leaf('e', 2), Leaf('t', 3), Leaf('x', 4),Leaf('a', 5), Leaf('b', 7))


val b = combine(leaflist)

val d =combine(b)

combine(d).length


val a = List(1)
a.head
val f = combine(leaflist)
combine(f)

val tt = decodedSecret

val en = encode(frenchCode)(tt)

en == secret




convert(frenchCode)

encode(frenchCode)(List('s'))
encode(frenchCode)(List('x'))



quickEncode(frenchCode)(tt)




b.sortBy{ case x => weight(x)}