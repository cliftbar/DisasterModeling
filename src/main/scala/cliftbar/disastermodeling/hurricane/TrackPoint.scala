package cliftbar.disastermodeling.hurricane

import java.time.LocalDateTime

/**
  * Created by cameron.barclift on 5/15/2017.
  */
case class TrackPoint(
                       catalogNumber:Option[Int]
                       , stormName:String
                       , basin:Option[String]
                       , timestamp:LocalDateTime
                       , eyeLat_y:Double
                       , eyeLon_x:Double
                       , maxWind_kts:Option[Double]
                       , minCp_mb:Option[Double]
                       , sequence:Double
                       , var fSpeed_kts:Double
                       , var isLandfallPoint:Boolean
                       , rMax_nmi:Double
                       , gwaf:Double
                       , var heading:Option[Double]
)
