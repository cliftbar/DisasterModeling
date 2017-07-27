package cliftbar.disastertest.hurricane.nws23

import org.scalatest.FunSuite
import cliftbar.disastermodeling.hurricane.nws23.model

//https://www.mt-oceanography.info/Utilities/coriolis.html
class CoriolisFrequencyTest extends FunSuite {
  test("Lat 15") {
    val result = model.coriolisFrequency(15) //* 10000 // removing scientific notation
    // Round to 3 decimal places
    assert(0.00003754 < result) // result is within acceptable error of 0.0000376
    assert(result < 0.00003765) // result is within acceptable error of 0.0000376
  }
}
