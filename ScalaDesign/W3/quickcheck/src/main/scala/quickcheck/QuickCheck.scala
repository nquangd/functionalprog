package quickcheck

import common._

import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._

abstract class QuickCheckHeap extends Properties("Heap") with IntHeap {

  lazy val genHeap: Gen[H] = for {
    x <- arbitrary[Int]
    heap <- oneOf(genHeap, const(empty))
  } yield insert(x, heap)


  implicit lazy val arbHeap: Arbitrary[H] = Arbitrary(genHeap)

  property("gen1") = forAll { (h: H) =>
    val m = if (isEmpty(h)) 0 else findMin(h)
    findMin(insert(m, h)) == m
  }

  property("insert two elems") = forAll { (a: Int, b:Int) =>
    val h = insert(b, insert(a, empty))
    findMin(h) == (if (a<b) a else b)
  }

  property("delete min") = forAll { (a: Int) =>
    val h = insert(a, empty)
    deleteMin(h) == empty
  }

  def findminlist(hp:H):List[Int] = {
    if (isEmpty(hp)) List() else
    {
      findMin(hp)::findminlist(deleteMin(hp))
    }
  }
  property("sorted") = forAll { (h: H) =>

    val l = findminlist(h)

    l == l.sorted
  }


  property("min meld") = forAll { (h1: H, h2: H) =>

    val m1 = findMin(h1)
    val m2 = findMin(h2)
    findMin(meld(h1, h2)) == (if (m1 < m2) m1 else m2)

  }


  property("three meld") = forAll { (h1:H, h2:H, h3:H) =>
    val a = meld(meld(h1, h2), h3)
    val b = meld(h1, meld(h2, h3))
    findminlist(a) == findminlist(b)
  }

}
