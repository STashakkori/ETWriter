package moneysaver

import javax.swing.*
import java.awt.*

class GraphicalView(private val cmdFactory: CommandFactory,
                    private val execContext: ExecutionContext) : JFrame() {

  init {
    title = "ETWriter"
    defaultCloseOperation = EXIT_ON_CLOSE
    setSize(800, 600)
    isResizable = true
    val screenSize = Toolkit.getDefaultToolkit().screenSize
    val x = (screenSize.width - width) / 2
    val y = (screenSize.height - height) / 2
    location = Point(x, y)
    minimumSize = Dimension(800, 600)
    val tabbedPane =  JTabbedPane()

    val fileManagerTab = FileManagerTab()
    val textEditorTab = TextEditorTab()
    //val settingsTab = SettingsTab()
    //val helpTab = HelpTab()

    @Suppress("UNUSED_VARIABLE")
    val emController = EditorManagerController(fileManagerTab, textEditorTab,
                                               cmdFactory, execContext)

    val microSalvumTab = MicroSalvumTab(cmdFactory, execContext)

    tabbedPane.addTab("Text Editor", null, textEditorTab, "Switch to Text Editor")
    tabbedPane.addTab("File Manager", null, fileManagerTab, "Switch to File Manager")
    tabbedPane.addTab("microSalvum", null, microSalvumTab, "Switch to microSalvum")
    //tabbedPane.addTab("Settings", null, settingsTab, "Go to Settings")
    //tabbedPane.addTab("Help", null, helpTab, "Click for Help")

    contentPane.add(tabbedPane)

    revalidate()
    repaint()
    isVisible = true
  }
}