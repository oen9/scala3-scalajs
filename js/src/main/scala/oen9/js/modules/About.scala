package oen9.js.modules

import slinky.core.annotations.react
import slinky.core.FunctionalComponent
import slinky.web.html._
import oen9.js.bridges.reactchartjs2._
import scala.scalajs.js
import slinky.core.facade.Hooks._

@react object About {
  type Props = Unit

  val chartData = js.Dynamic.literal(
    labels = js.Array('1', '2', '3', '4', '5', '6'),
    datasets = js.Array(
      js.Dynamic.literal(
        label = "# of Votes",
        data = js.Array(12, 19, 3, 5, 2, 3),
        fill = false,
        backgroundColor = "rgb(255, 99, 132)",
        borderColor = "rgba(255, 99, 132, 0.2)"
      )
    )
  )

  //val lineData = js.Dynamic.literal(
  //  datasets = js.Array(
  //    js.Dynamic.literal(
  //      data = js.Array(
  //        js.Dynamic.literal(x = "1", y = 65),
  //        js.Dynamic.literal(x = "2", y = 59),
  //        js.Dynamic.literal(x = "3", y = 80),
  //        js.Dynamic.literal(x = "4", y = 81),
  //        js.Dynamic.literal(x = "5", y = 56),
  //        js.Dynamic.literal(x = "6", y = 55),
  //        js.Dynamic.literal(x = "7", y = scala.util.Random.nextInt(100))
  //      ),
  //      fill = false,
  //      borderColor = "rgb(75, 192, 192)",
  //      tension = 0.1
  //    )
  //  )
  //)

  val component = FunctionalComponent[Props] { _ =>
    val (state, setState) = useState(8)
    val (dataArr, setDataArr) = useState(
      js.Array(
        js.Dynamic.literal(x = "1", y = 65),
        js.Dynamic.literal(x = "2", y = 59),
        js.Dynamic.literal(x = "3", y = 80),
        js.Dynamic.literal(x = "4", y = 81),
        js.Dynamic.literal(x = "5", y = 56),
        js.Dynamic.literal(x = "6", y = 55),
        js.Dynamic.literal(x = "7", y = scala.util.Random.nextInt(100))
      )
    )

    def addNext(): Unit = {
      println(state)
      val newRecord   = js.Dynamic.literal(x = s"$state", y = scala.util.Random.nextInt(100))
      val nextDataArr = dataArr.addOne(newRecord)
      setState(state + 1)
      setDataArr(nextDataArr)
    }

    val lineDataLive = js.Dynamic.literal(
      datasets = js.Array(
        js.Dynamic.literal(
          data = dataArr,
          fill = false,
          borderColor = "rgb(75, 192, 192)",
          tension = 0.1
        )
      )
    )

    div(
      div("about"),
      button(className := "btn btn-primary", onClick := addNext _, "click"),
      Line(lineDataLive),
      Doughnut(data = chartData)
    )
  }
}
