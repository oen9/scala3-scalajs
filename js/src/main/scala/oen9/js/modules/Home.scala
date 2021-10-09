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
          label = "my data",
          data = dataArr,
          fill = false,
          borderColor = "rgb(75, 192, 192)",
          backgroundColor = "rgb(255, 99, 132)",
          tension = 0.1
        )
      )
    )
    div(
      div(className := "row", Line(lineDataLive)),
      div(className := "row", button(className := "btn btn-primary", onClick := addNext _, "add more data"))
    )
  }
}
