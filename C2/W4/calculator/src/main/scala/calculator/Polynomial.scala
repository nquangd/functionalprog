package calculator

object Polynomial {
  def computeDelta(a: Signal[Double], b: Signal[Double],
      c: Signal[Double]): Signal[Double] = Signal {
    val bvalue = b()
    bvalue*bvalue - 4*(a()*c())
  }

  def computeSolutions(a: Signal[Double], b: Signal[Double],
      c: Signal[Double], delta: Signal[Double]): Signal[Set[Double]] = Signal{
    delta() match {
      case c if c <0 => Set()
      case  c if c == 0 => Set(-b()/2/a())
      case _ => {
        val sqrtdelta = math.sqrt(delta())
        val minusb = -b()
        val twoa = 2*a()
        Set((minusb + sqrtdelta)/twoa, (minusb-sqrtdelta)/twoa)}
    }
  }
}
