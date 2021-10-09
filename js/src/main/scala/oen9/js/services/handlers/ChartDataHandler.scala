package oen9.js.services.handlers

import diode.data.Pot
import diode.data.PotAction
import diode.{ActionHandler, ModelRW}
import oen9.js.services.Clicks
import oen9.js.services.Actions.GenerateMoreChartData
import oen9.js.bridges.reactchartjs2.DataRecord
import scala.scalajs.js.Date

class ChartDataHandler[M](modelRW: ModelRW[M, Seq[DataRecord]]) extends ActionHandler(modelRW) {
  override def handle = { case GenerateMoreChartData =>
    val nextId      = (new Date()).toISOString()
    val randomValue = scala.util.Random.nextInt(100)
    val newRecord   = DataRecord(x = s"$nextId", y = randomValue)
    val newValue    = value :+ newRecord
    updated(newValue)
  }
}
