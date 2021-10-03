package oen9.js.modules

import diode.data.Pot
import diode.data.PotState.PotReady
import slinky.core.annotations.react
import slinky.core.facade.Fragment
import slinky.core.facade.ReactElement
import slinky.core.FunctionalComponent
import slinky.reactrouter.Link
import slinky.web.html._

@react object Layout {
  case class Props(content: ReactElement)

  val component = FunctionalComponent[Props] { props =>
    Fragment(
      div(
        props.content,
        div("© 2020 oen")
      )
    )
  }
}
