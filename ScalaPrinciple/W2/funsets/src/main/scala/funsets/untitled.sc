

type Set = Int => Boolean

def contains(s: Set, elem: Int): Boolean = s(elem)
def singletonSet(elem: Int): Set = {
  x: Int => if (x==elem) true else false
}


def union(s: Set, t: Set): Set = {
  x: Int => contains(s, x)|contains(t,x)
}



def intersect(s: Set, t: Set): Set = {
  x: Int => contains(s, x) && contains(t,x)
}


def diff(s: Set, t: Set): Set = {
  x: Int => contains(s, x) && !contains(t,x)
}


def filter(s: Set, p: Int => Boolean): Set = {
  x:Int => contains(s,x) && p(x)
}


val bound = 100

def forall(s: Set, p: Int => Boolean): Boolean = {
  def iter(a: Int): Boolean = {
    if (a > bound) true
    else
      if (contains(s,a) && !p(a)) false
      else iter(a+1)
  }
  iter(-10)

}

def forall2(s: Set, p: Int => Boolean): Boolean = {
  def iter(a: Int): Boolean = {
    if (a > bound) true
    else
    if (contains(s,a) && p(a)) true
    else iter(a+1)
  }
  iter(-10)

}


def exists(s: Set, p: Int => Boolean): Boolean = {
  val pp: Int => Boolean = x => !p(x)
  if (!forall(s,pp)) true else false
}


def map(s: Set, f: Int => Int): Set = {
  x:Int => if (exists(s, y => x == f(y))) true else false
}

val A = singletonSet(6)
contains(A,7)

val B = singletonSet(8)
val U = union(A,B)
val I = intersect(A,U)

val D = diff(A,B)

contains(U, 7)
contains(I,6)
contains (D, 6)

val pp: Int => Boolean = x => x % 2 == 1

val F = filter(U,pp)

contains(F, 5)
contains(F,6)


forall(U,pp)

val ppp: Int => Boolean = x => (x % 2 == 1)

val pppp: Int => Boolean = x => !ppp(x)


exists(U,ppp)

val fu: Int => Int = x => x * x

val mm = map(B, x=>2*x+1)
contains(mm,17)