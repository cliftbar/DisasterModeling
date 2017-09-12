package cliftbar.disastertest.hurricane.nws23

import java.time.LocalDateTime

import cliftbar.disastermodeling.hurricane.nws23.{HurricaneEvent, LatLonGrid, TrackPoint}
import org.scalatest.FunSuite

class ScratchTest extends FunSuite {
  test("Scratch Test"){
    val tp:TrackPoint = new TrackPoint(
      Some(2)
      ,"TestStorm"
      ,Some("AL")
      ,LocalDateTime.of(2017, 1, 1, 12, 0)
      , 22.2
      , -97.6
      , Some(80)
      , null
      , 0
      , 15
      , false
      , 15
      , 0.9
      , Some(0.0)
    )

    val tpList = List(tp)

    val grid:LatLonGrid = new LatLonGrid(
      24.2
      ,20.2
      ,-99.6
      ,-95.6
      ,10
      ,10
    )

    val rMax = 15
    val event = new HurricaneEvent(grid, tpList, rMax)

    event.DoCalcs(360, 2)
    //println(event.CalcedResults(0)._1)

    //event.ConvertGridToContours(event.CalcedResults, grid.GetWidthInBlocks, grid.GetHeightInBlocks, 30)
  }

}
