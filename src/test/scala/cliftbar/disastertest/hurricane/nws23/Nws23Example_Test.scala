package cliftbar.disastertest.hurricane.nws23

import java.time.LocalDateTime

import org.scalatest.FunSuite
import cliftbar.disastermodeling.hurricane.nws23.{HurricaneEvent, LatLonGrid, TrackPoint, model}

class Nws23Example_Test extends FunSuite{

  // NWS 23 Example Parameters
  val lat = 33.5
  val peripheralPressure = 30.12
  val centralPressure = 26.31
  val radiusMaxWind = 15
  val forwardSpeed = 10
  val trackDirection = 180
  val gwaf = 0.95

  // NWS 23 Example Values
  val densityCoef = 68.8
  val maxGradientWind = 132.1

  val threshold = 0.02 // match calculations within +/- 2% of NWS 23 Example


  test("NWS 23 Example - Coriolis Parameter") {
    val coriolisParameter = model.coriolisFrequency(lat) * 3600 // get in 1/hr

    // Round to 0.29
    assert(0.285 <= coriolisParameter)
    assert(coriolisParameter < 0.295)
  }

  test("NWS 23 Example - Density Coefficient") {
    val calcDensityCoef = model.kDensityCoefficient(lat)
    assert(densityCoef === (calcDensityCoef * 10).round / 10.0)
  }

  test("NWS 23 Example - Maximum Gradient Wind") {
    val calcMaxGradientWind = model.gradientWindAtRadius(peripheralPressure, centralPressure, radiusMaxWind, lat)
    assert(maxGradientWind * (1 - threshold) < calcMaxGradientWind)
    assert(calcMaxGradientWind < maxGradientWind * (1 + threshold))
  }

  test("NWS 23 Example - Wind Profile For Stationary Hurricane") {
    val windProfile = Map(15 -> 1.0, 30 -> 0.870, 60 -> 0.590, 100 -> 0.428, 200 -> 0.250, 300 -> 0.158)

    for ((r, ws) <- windProfile) {
      println(s"Testing r: $r, ws: $ws")
      val calcWs = model.radialDecay(r, radiusMaxWind)
      assert(ws * (0.85) < calcWs)
      assert(calcWs < ws * (1.15))
    }
  }
}
