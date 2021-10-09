package oen9.js.services

import diode.data.Empty
import diode.data.Pot
import diode.data.PotAction
import diode.{Action, Circuit}
import oen9.js.services.handlers.ChartDataHandler
import oen9.js.bridges.reactchartjs2.DataRecord

case class RootModel(
  chartDataRecords: Seq[DataRecord]
)
case class Clicks(count: Int)

object AppCircuit extends Circuit[RootModel] {
  override protected def initialModel: RootModel = RootModel(
    chartDataRecords = Seq(
      DataRecord(x = "10h 50m 49s:112", y = 65),
      DataRecord(x = "10h 50m 50s:112", y = 59),
      DataRecord(x = "10h 50m 51s:112", y = 80),
      DataRecord(x = "10h 50m 52s:112", y = 81),
      DataRecord(x = "10h 50m 53s:112", y = 56),
      DataRecord(x = "10h 50m 54s:112", y = 55)
    )
  )

  override protected def actionHandler: AppCircuit.HandlerFunction = composeHandlers(
    new ChartDataHandler(zoomTo(_.chartDataRecords))
  )
}
