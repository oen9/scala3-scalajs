package oen9.js.services.handlers

import diode.data.Pot
import diode.data.PotAction
import diode.{ActionHandler, ModelRW}
import oen9.js.services.Clicks
import oen9.js.services.Actions.IncreaseClicks

class ClicksHandler[M](modelRW: ModelRW[M, Clicks]) extends ActionHandler(modelRW) {
  override def handle = { case IncreaseClicks =>
    updated(value.copy(count = value.count + 1))
  }
}
