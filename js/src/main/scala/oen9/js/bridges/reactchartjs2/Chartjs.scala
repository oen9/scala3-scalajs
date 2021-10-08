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

@react object Doughnut extends ExternalComponent {
  case class Props(data: js.Object)
  override val component = ReactChartjs2.Doughnut
}

@react object Line extends ExternalComponent {
  case class Props(data: js.Object)
  override val component = ReactChartjs2.Line
}

//@js.native trait MyRefApi extends js.Object {
//  def someFunction(): Unit = js.native
//}
