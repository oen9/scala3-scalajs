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
      DataRecord(x = "2021-10-04T11:15:43.012Z", y = 65),
      DataRecord(x = "2021-10-05T11:15:43.012Z", y = 59),
      DataRecord(x = "2021-10-06T11:15:43.012Z", y = 80),
      DataRecord(x = "2021-10-07T11:15:43.012Z", y = 81),
      DataRecord(x = "2021-10-08T11:15:43.012Z", y = 56),
      DataRecord(x = "2021-10-09T11:15:43.012Z", y = 55)
    )
  )

  override protected def actionHandler: AppCircuit.HandlerFunction = composeHandlers(
    new ChartDataHandler(zoomTo(_.chartDataRecords))
  )
}
