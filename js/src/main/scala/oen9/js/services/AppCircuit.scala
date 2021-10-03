package oen9.js.services

import diode.data.Empty
import diode.data.Pot
import diode.data.PotAction
import diode.{Action, Circuit}
import oen9.js.services.handlers.ClicksHandler

case class RootModel(
  clicks: Clicks
)
case class Clicks(count: Int)

object AppCircuit extends Circuit[RootModel] {
  override protected def initialModel: RootModel = RootModel(Clicks(0))

  override protected def actionHandler: AppCircuit.HandlerFunction = composeHandlers(
    new ClicksHandler(zoomTo(_.clicks))
  )
}
