package cliftbar.disastermodeling.hurricane

import scala.annotation.tailrec

package object model {

  var xTwo:Double = 25

  /**
    * Calculate Radius of Max Wind from max windspeed and latitude
    * Equation 7a
    * @param vMax_mps Maximum windspeed in m/s
    * @param lat_deg Latitude in decimal degrees
    * @return Radius of max wind in kilometers
    */
  def calculateRmax(vMax_mps:Double, lat_deg:Double):Double = {
    return 46.4 * math.exp((-0.0155 * vMax_mps) + (0.0169 * lat_deg))
  }

  /**
    * Calculation for X1 parameter for the double exponential method
    * Equation 10a, Page 1114, pdf Page 12
    * @param vMax_mps Maximum windspeed in m/s
    * @param lat_deg Latitude in decimal degrees
    * @return
    */
  def parameterXone(vMax_mps:Double, lat_deg:Double):Double = {
    return 317.1 - (2.026 * vMax_mps) + (1.915 * lat_deg)
  }

  /**
    * Calculation for the n parameter for the double exponential method
    * Equation 10b, Page 1114, pdf Page 12
    * @param vMax_mps Maximum windspeed in m/s
    * @param lat_deg Latitude in decimal degrees
    * @return
    */
  def parameterN(vMax_mps:Double, lat_deg:Double):Double = {
    return 0.4067 + (0.0144 * vMax_mps) - (0.0038 * lat_deg)
  }

  /**
    * Calculation of the A parameter for the double exponential method
    * 0 <= A
    * Equation 10c, Page 1114, pdf Page 12
    * @param vMax_mps Maximum windspeed in m/s
    * @param lat_deg Latitude in decimal degrees
    * @return
    */
  def parameterA(vMax_mps:Double, lat_deg:Double):Double = {
    return math.max(0.0696 + (0.0049 * vMax_mps) - (0.0064 * lat_deg), 0)
  }

  def parameterXi(r_km:Double, rOne:Double, rTwo:Double):Double = {
    val xi = (r_km - rOne) / (rTwo - rOne)
  }

  def weightingFunction(xi:Double):Double = {
    return (70 * math.pow(xi, 9)) - (315 * math.pow(xi, 8)) + (540 * math.pow(xi, 7)) - (420 * math.pow(xi, 6)) + (126 * math.pow(xi, 5))
  }

  def weightingFunctionDerivative(xi:Double):Double = {
    return (630 * math.pow(xi, 8)) - (2520 * math.pow(xi, 7)) + (3780 * math.pow(xi, 6)) - (2520 * math.pow(xi, 5)) + (630 * math.pow(xi, 4))
  }

  @tailrec
  def recursiveNewtonRaphsonSearch(xi:Double, iteration:Int, params:Map[String, Double]):Double = {
    val wFunc = weightingFunction(xi) - params("rightTerm")
    val wFuncDeriv = weightingFunctionDerivative(xi)
    val xiNew = xi - (wFunc / wFuncDeriv)
    if (math.abs(xiNew - xi) < params("errorThreshold")) {
      xi
    } else if (iteration > params("maxIterations")) {
      println("Iteration Limit Reached")
      xi
    } else {
      recursiveNewtonRaphsonSearch(xiNew, iteration + 1, params)
    }
  }

  def rightTerm(n:Double, xOne:Double, xTwo:Double, A:Double, rMax:Double):Double = {
    val duplicateTerm = (n * (((1 - A) * xOne) + (A * xTwo)))
    return duplicateTerm / (duplicateTerm + rMax)
  }

  def xiToRadii(xi:Double, rMax:Double, transitionWidth_km:Double = Double.NaN):(Double, Double) = {

    val localTransition_km = if (transitionWidth_km.isNaN) { 25 } else { transitionWidth_km }

    //R1: Start of Transition Zone
    val rOne = rMax - (xi * localTransition_km)

    //R2: End of Transition Zone
    val rTwo = rOne + localTransition_km
    return (rOne, rTwo)
  }

  def transitionZoneCalc(n:Double, xOne:Double, xTwo:Double, A:Double, rMax:Double, initial_xi:Double = 0.5, errorThreshold:Double = 0.00001, maxIterations:Int = 1000, transitionWidth:Double = 25):(Double, Double) = {
    val rightTerm = rightTerm(n, xOne, xTwo, A, rMax)
    val params = Map("rightTerm" -> 0, "errorThreshold" -> errorThreshold, "maxIterations" -> maxIterations)
    val convergedXi = recursiveNewtonRaphsonSearch(initial_xi, 0, params)

    val R = xiToRadii(convergedXi, rMax, transitionWidth)

    return R
  }

  def parameterVi(vMax_mps:Double, r_km:Double, rMax_km:Double, n:Double):Double = {
    math.pow((vMax_mps * (r_km / rMax_km)), n)
  }

  def parameterVo(vMax_mps:Double, r_km:Double, rMax_km:Double, xOne:Double, xTwo:Double, A:Double):Double = {
    vMax_mps * (((1 - A) * math.exp(-1.0 * (r_km * rMax_km) / xOne)) + A * math.exp(-1 * (r_km - rMax_km) / xTwo))
  }

  def calcGradientWindSpeed(vMax_mps:Double, r_km:Double, lat_deg:Double, xTwo:Double = 25, rMax_km:Double = Double.NaN):Double = {
    val n = parameterN(vMax_mps, lat_deg)
    val A = parameterA(vMax_mps, lat_deg)
    val xOne = parameterXone(vMax_mps, lat_deg)
    val R = transitionZoneCalc(n, xOne, xTwo, A, rMax_km)

    val vGrad = if (r_km < R._1) {
      parameterVi(vMax_mps, r_km, rMax_km, n)
    } else if (r_km < R._2) {
      val xi = parameterXi(r_km, R._1, R._2)
      val w = weightingFunction(xi)
      val vi = parameterVi(vMax_mps, r_km, rMax_km, n)
      val vo = parameterVo(vMax_mps, r_km, rMax_km, xOne, xTwo, A)

      (vi * (1 - w)) + (vo * w)
    } else {
      parameterVo(vMax_mps, r_km, rMax_km, xOne, xTwo, A)
    }

    return vGrad
  }


}
