import forcomp.Anagrams._


wordOccurrences("abcssdddc")

sentenceOccurrences(List("I","love","you"))

val s = List("I","love","you")

s reduceLeft (_++_)

"word".isEmpty

//val d = dictionary take 10

val d = List("eat","ate","tea","love","you","boy")

val ss = d zip (d map(x => wordOccurrences(x)))

ss groupBy(_._2)

d groupBy((element: String) => wordOccurrences(element))


wordAnagrams("tea")


combiAcc(List(('a', 2), ('b', 2)))



List(('a', 2), ('b', 2)).tail

