package oen9.js.modules

import slinky.core.annotations.react
import slinky.core.FunctionalComponent
import slinky.web.html._

@react object Home {
  type Props = Unit
  val component = FunctionalComponent[Props] { _ =>
    div("home")
  }
}
