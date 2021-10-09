package oen9.js.modules

import slinky.core.annotations.react
import slinky.core.FunctionalComponent
import slinky.web.html._
import oen9.js.bridges.reactchartjs2._
import scala.scalajs.js
import slinky.core.facade.Hooks._

@react object Home {
  type Props = Unit

  val component = FunctionalComponent[Props] { _ =>
    val (state, setState) = useState(8)
    val (dataArr, setDataArr) = useState(
      Seq(
        DataRecord(x = "1", y = 65),
        DataRecord(x = "2", y = 59),
        DataRecord(x = "3", y = 80),
        DataRecord(x = "4", y = 81),
        DataRecord(x = "5", y = 56),
        DataRecord(x = "6", y = 55),
        DataRecord(x = "7", y = scala.util.Random.nextInt(100))
      )
    )

    def addNext(): Unit = {
      println(state)
      val newRecord   = DataRecord(x = s"$state", y = scala.util.Random.nextInt(100))
      val nextDataArr = dataArr :+ newRecord
      setState(state + 1)
      setDataArr(nextDataArr)
    }

    val dataSet = DataSet(
      data = dataArr,
      label = "my data",
      borderColor = "rgb(75, 192, 192)",
      backgroundColor = "rgb(255, 99, 132)",
      tension = 0.1
    )
    val chartData = ChartData(datasets = Seq(dataSet))
    div(
      div(className := "row", Line(chartData)),
      div(className := "row", button(className := "btn btn-primary", onClick := addNext _, "add more data"))
    )
  }
}
