import streams._

import streams.StringParserTerrain
import streams.Bloxorz
import streams.Solver

val f = Vector(Vector('S', 'T'), Vector('o', 'o'), Vector('o', 'o'))
f.length
f(0)(1)
f(1).length

val cc = for {
  r <- 0 until f.length
  c <- 0 until f(r).length
} yield (r,c)

val x = (y:((Int,Int))) => {

  val ccc = for {
    r <- 0 until f.length
    if y._1 <= f.length-1
    c <- 0 until f(r).length
    if y._2 <= f(r).length -1

  } yield true

  //ccc contains y

}

x((0,0))



val r = f indexWhere(x => x contains 'S')

val c = f(r) indexOf('S')

val l = List(1,2)

3::l

(1,2) == (1,2)


trait fortest extends GameDef with Solver with StringParserTerrain {
  val tt = neighborsWithHistory(Block(Pos(1,1),Pos(1,1)), List(Left,Up))
}

