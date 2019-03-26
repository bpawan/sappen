package de.sappen

import java.io.File

import javax.sound.sampled._

class Player {

  def play(filePathName: String): Unit = {

    val audioStream: AudioInputStream = createAudioStream(filePathName)

    val format = audioStream.getFormat

    doPlay(audioStream, format, getDataLine(format))
  }

  private def doPlay(audioStream: AudioInputStream, format: AudioFormat, line: SourceDataLine): Unit = {
    line.open(format)
    line.start()

    val buffer = new Array[Byte](line.getBufferSize)

    Stream
      .continually(audioStream.read(buffer, 0, buffer.length))
      .takeWhile(_ >= 0)
      .foreach { bytesRead =>
        Stream
          .continually(line.write(buffer, 0, bytesRead))
          .takeWhile(_ < bytesRead)
      }

    line.drain()
    line.stop()
    line.close()
  }

  private def getDataLine(format: AudioFormat): SourceDataLine =
    AudioSystem.getLine(new DataLine.Info(classOf[SourceDataLine], format)).asInstanceOf[SourceDataLine]

  private def createAudioStream(filePathName: String): AudioInputStream =
    AudioSystem.getAudioInputStream(new File(filePathName))
}
