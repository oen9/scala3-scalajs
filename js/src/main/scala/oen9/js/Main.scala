package oen9

import org.scalajs.dom.document
import org.scalajs.dom
import scala.scalajs.LinkingInfo

object Main {
  def main(args: Array[String]): Unit = {
    if (LinkingInfo.developmentMode) {
      println("dev mode")
    }
    val text = "Hello world!"
    println(text)

    val target   = document.getElementById("main")
    val textNode = document.createElement("div")
    textNode.textContent = text
    target.appendChild(textNode)
  }
}
