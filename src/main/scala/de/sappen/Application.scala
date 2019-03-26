package de.sappen

object Application extends App {

  val filePathName = "./crowd_outside_1.wav"

  val player = new Player()

  player.play(filePathName)
}
