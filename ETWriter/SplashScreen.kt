package moneysaver

import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.util.concurrent.CountDownLatch

class SplashScreen(private val resourcePath: String,
                   private val durationMillis: Long) : JWindow() {
  private val latch = CountDownLatch(1)

  init {
    //val splashImage = Toolkit.getDefaultToolkit().createImage("ETSplash.png")
    val splashLabel = JLabel(ImageIcon(this.javaClass.getResource(resourcePath)))
    //val splashLabel = JLabel(ImageIcon(splashImage))
    contentPane.add(splashLabel)
    pack()
    centerOnScreen()
  }

  private fun centerOnScreen() {
    val screenDim = Toolkit.getDefaultToolkit().screenSize
    val x = (screenDim.width - width) / 2
    val y = (screenDim.height - height) / 2
    setLocation(x, y)
  }

  fun showSplashScreen() {
    isVisible = true
    Timer(durationMillis.toInt(), ActionListener {
        isVisible = false
        dispose()
        latch.countDown() // Notify that SplashScreen has finished
    }).apply {
        isRepeats = false // Make it run only once
        start()
    }
  }

  fun waitForSplashScreen() {
    latch.await()
  }
}