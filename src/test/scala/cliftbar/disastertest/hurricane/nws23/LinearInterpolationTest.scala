package cliftbar.disastertest.hurricane.nws23

import cliftbar.disastermodeling.hurricane.nws23.model
import org.scalatest.FunSuite

class LinearInterpolationTest extends FunSuite {

  test("Positive Slop: Linear Interpolation between (-3,-3) and (3,3) at 1 equals 1") {

    val result = model.linearInterpolation(-3,-3,3,3,1)
    assert(result === 1)
  }

  test("Negative Slope: Linear Interpolation between (3,3) and (-3,-3) at 1 equals 1") {

    val result = model.linearInterpolation(3,3,-3,-3,1)
    assert(result === 1)
  }

  test("Past Range Positive: Linear Interpolation between (-3,-3) and (3,3) at 5 equals 5") {

    val result = model.linearInterpolation(-3,-3,3,3,5)
    assert(result === 5)
  }

  test("Past Range Negative: Linear Interpolation between (3,3) and (-3,-3) at -5 equals -5") {

    val result = model.linearInterpolation(3,3,-3,-3,-5)
    assert(result === -5)
  }

  test("No X Movement: Linear Interpolation between (3,3) and (3,3) at 3 equals NaN") {

    val result = model.linearInterpolation(3,3,3,3,3)
    assert(result.isNaN)
  }
}