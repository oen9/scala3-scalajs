package oen9.js.bridges.reactchartjs2

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import slinky.core.ExternalComponent
import slinky.core.annotations.react

@JSImport("react-chartjs-2", JSImport.Namespace)
@js.native
object ReactChartjs2 extends js.Object {
  val Doughnut: js.Object = js.native
  val Line: js.Object     = js.native
}

case class DataRecord(x: String, y: Double)
case class DataSet(
  data: Seq[DataRecord],
  label: String = "",
  fill: Boolean = false,
  borderColor: String = "",
  backgroundColor: String = "",
  tension: Double = 0
)
case class ChartData(datasets: Seq[DataSet])

@react object Doughnut extends ExternalComponent {
  case class Props(data: ChartData)
  override val component = ReactChartjs2.Doughnut
}

@react object Line extends ExternalComponent {
  case class Props(data: ChartData)
  override val component = ReactChartjs2.Line
}

//@js.native trait MyRefApi extends js.Object {
//  def someFunction(): Unit = js.native
//}
