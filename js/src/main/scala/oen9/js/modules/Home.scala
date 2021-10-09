package oen9.js.modules

import slinky.core.annotations.react
import slinky.core.FunctionalComponent
import slinky.web.html._
import oen9.js.bridges.reactchartjs2._
import scala.scalajs.js
import slinky.core.facade.Hooks._
import oen9.js.services.AppCircuit
import oen9.js.services.ReactDiode
import oen9.js.services.Actions.GenerateMoreChartData

@react object Home {
  type Props = Unit

  val component = FunctionalComponent[Props] { _ =>
    val (chartDataRecords, dispatch) = ReactDiode.useDiode(AppCircuit.zoomTo(_.chartDataRecords))

    def generateMoreData(): Unit = dispatch(GenerateMoreChartData)
    val dataSet = DataSet(
      data = chartDataRecords,
      label = "my data",
      borderColor = "rgb(75, 192, 192)",
      backgroundColor = "rgb(255, 99, 132)",
      tension = 0.1
    )

    val chartData = ChartData(datasets = Seq(dataSet))
    div(
      div(className := "row", Line(chartData)),
      div(className := "row", button(className := "btn btn-primary", onClick := generateMoreData _, "add more data"))
    )
  }
}
