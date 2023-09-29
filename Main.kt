package moneysaver

import javax.swing.SwingUtilities
import javax.swing.UIManager
import javax.swing.plaf.ColorUIResource
import java.awt.Color
import javax.swing.JFrame
import javax.swing.JButton
import javax.swing.JPanel

fun main(args: Array<String>) {
  val cmdFactory = CommandFactory()
  val execContext = ExecutionContext()

  if (args.size < 2) {
    val splash = SplashScreen("/ETSplash.png", 2000)
    splash.showSplashScreen()
    splash.waitForSplashScreen()

    val lightBackground = Color(200, 200, 200)
    
    UIManager.put("Panel.background", lightBackground)
    UIManager.put("Label.foreground", Color.BLACK)

    SwingUtilities.invokeLater {
      GraphicalView(cmdFactory, execContext)
    }
  }
  else {
    val consoleView = ConsoleView()
    val command = cmdFactory.createCommand(args)
    consoleView.displayAllErrors()
    consoleView.displayCommandOutput(execContext.run(command))
  }
}