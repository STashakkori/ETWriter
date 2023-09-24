package moneysaver

import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class GraphicalView(private val cmdFactory: CommandFactory,
                    private val execContext: ExecutionContext) : JFrame() {

  private val commandComboBox: JComboBox<String> = JComboBox(arrayOf("Command 1",
                                                                     "Command 2",
                                                                     "Command 3"))
  private val sourcePathChooser: JFileChooser = JFileChooser()
  private val backupPathChooser: JFileChooser = JFileChooser()

  private val textMatchButton: JButton = JButton("TextMatch")
  private val textInsertButton: JButton = JButton("TextInsert")
  private val executeButton: JButton = JButton("Execute")
  private val backupButton: JButton = JButton("Backup")
  private val cancelButton: JButton = JButton("Cancel")

  private val openButton: JButton = JButton("Open")
  private val saveButton: JButton = JButton("Save")
  //private val commentButton: JButton = JButton("Comment")
  private val maxSizeField: JComboBox<String> = JComboBox(arrayOf("10MB dirmax",
                                                                  "1KB dirmax",
                                                                  "100KB dirmax",
                                                                  "500KB dirmax",
                                                                  "1MB dirmax",
                                                                  "1GB dirmax"))
  private val lineNumberField: JTextField = JTextField()
  //private val extensionComboBox: JComboBox<String> = JComboBox(arrayOf(".txt", ".docx", ".csv"))
  //private val statusField: JTextField = JTextField()
  //private val errorTextArea: JTextArea = JTextArea()
  //private val textToFindTextArea: JTextArea = JTextArea()
  //private val textToInsertTextArea: JTextArea = JTextArea()

  init {
    title = "ETWriter"
    defaultCloseOperation = EXIT_ON_CLOSE
    setSize(800, 600)
    isResizable = true
    val screenSize = Toolkit.getDefaultToolkit().screenSize
    val x = (screenSize.width - width) / 2
    val y = (screenSize.height - height) / 2
    location = Point(x, y)
    //setExtendedState(JFrame.MAXIMIZED_BOTH)

    minimumSize = Dimension(800, 600)
    //textToFindTextArea.preferredSize = Dimension(800, 900)
    //textToInsertTextArea.preferredSize = Dimension(800, 900)

    val tabbedPane =  JTabbedPane()

// Create the panel for the "Text Editor" tab
    val textEditorPanel = JPanel(GridBagLayout())
    val textEditorGbc = GridBagConstraints()
    textEditorGbc.insets = Insets(5, 5, 5, 5)

    // Create a row of buttons
    val buttonPanel = JPanel(GridBagLayout())

    textEditorGbc.gridx = 0 // Column
    textEditorGbc.gridy = 0 // Row
    textEditorGbc.gridwidth = 1
    buttonPanel.add(openButton, textEditorGbc)

    textEditorGbc.gridx = 1 // Column
    buttonPanel.add(saveButton, textEditorGbc)

    textEditorPanel.add(buttonPanel, textEditorGbc)

    val textArea = JTextArea()
    val scrollPane = JScrollPane(textArea)

    textEditorGbc.gridx = 0
    textEditorGbc.gridy = 1 // Move to the next row
    textEditorGbc.gridwidth = GridBagConstraints.REMAINDER
    textEditorGbc.weightx = 1.0
    textEditorGbc.weighty = 1.0
    textEditorGbc.fill = GridBagConstraints.BOTH
    textEditorPanel.add(scrollPane, textEditorGbc)

    val fileManagerPanel = JPanel(GridBagLayout())
    val scramblerPanel = JPanel(GridBagLayout())
    val gbc = GridBagConstraints()
    gbc.insets = Insets(0,0,0,0)

    val splash = this.javaClass.getResource("/ETSplash.png")
    //val splash = "ETSplash.png"
    val imageIcon = ImageIcon(splash)
    val image = imageIcon.image.getScaledInstance(200, 100,
                                                  Image.SCALE_SMOOTH)
    val scaledImageIcon = ImageIcon(image)
    val label = JLabel(scaledImageIcon)
    gbc.gridx = 0 // Column
    gbc.gridy = 0 // Row
    gbc.anchor = GridBagConstraints.LINE_START
    gbc.gridwidth = 1
    fileManagerPanel.add(label, gbc)

/*
    val nestedPanel = JPanel(GridBagLayout())
    gbc.gridx = 0
    gbc.gridy = 0
    nestedPanel.add(label, gbc)
*/

// Increment after here by 2
    //gbc.gridx = 1
    //gbc.gridy = 3
    //gbc.gridwidth = 1
    //fileManagerPanel.add(JLabel("Select Command:"), gbc)

    gbc.gridx = 1
    gbc.gridy = 1
    gbc.gridwidth = GridBagConstraints.LINE_START
    fileManagerPanel.add(commandComboBox, gbc)

    gbc.gridx = 1
    gbc.gridy = 2
    gbc.gridwidth = GridBagConstraints.LINE_START
    fileManagerPanel.add(maxSizeField, gbc)

    gbc.gridx = 1 // Column
    gbc.gridy = 3 // Row
    gbc.gridwidth = GridBagConstraints.LINE_START
    fileManagerPanel.add(backupButton, gbc)

    gbc.gridx = 0 // Column
    gbc.gridy = 1 // Row
    gbc.gridwidth = GridBagConstraints.LINE_START
    gbc.anchor = GridBagConstraints.LINE_END
    fileManagerPanel.add(textMatchButton, gbc)

    gbc.gridx = 0 // Column
    gbc.gridy = 2 // Row
    gbc.gridwidth = GridBagConstraints.LINE_START
    gbc.anchor = GridBagConstraints.LINE_END
    fileManagerPanel.add(textInsertButton, gbc)

    gbc.gridx = 0 // Column
    gbc.gridy = 3 // Row
    gbc.gridwidth = GridBagConstraints.LINE_START
    gbc.anchor = GridBagConstraints.LINE_END
    //gbc.weightx = 1.0 // Add this line to make it expand horizontally
    //gbc.weighty = 1.0 // Add this line to make it expand vertically
    //nestedPanel.add(executeButton, gbc)
    //fileManagerPanel.add(nestedPanel, gbc)
    fileManagerPanel.add(executeButton, gbc)

    gbc.gridx = 0
    gbc.gridy = 5
    gbc.gridwidth = GridBagConstraints.HORIZONTAL
    fileManagerPanel.add(sourcePathChooser, gbc)

    gbc.gridx = 1
    gbc.gridy = 0
    gbc.gridwidth = 10
    //gbc.gridwidth = GridBagConstraints.REMAINDER
    //gbc.fill = GridBagConstraints.HORIZONTAL
    //gbc.weightx = 1.0
    fileManagerPanel.add(lineNumberField, gbc)

/*
    gbc.gridx = 3
    gbc.gridy = 0
    gbc.gridwidth = GridBagConstraints.REMAINDER
    gbc.fill = GridBagConstraints.HORIZONTAL
    gbc.weightx = 1.0
    fileManagerPanel.add(statusField, gbc)

    gbc.gridx = 1
    gbc.gridy = 0
    gbc.gridwidth = 1
    fileManagerPanel.add(JLabel("Error:"), gbc)

    gbc.gridx = 2
    gbc.gridy = 12
    gbc.gridwidth = GridBagConstraints.REMAINDER
    gbc.fill = GridBagConstraints.HORIZONTAL
    gbc.weightx = 1.0
    fileManagerPanel.add(errorTextArea, gbc)

    gbc.gridx = 1
    gbc.gridy = 0
    gbc.gridwidth = 1
    fileManagerPanel.add(JLabel("Text to Find:"), gbc)

    //gbc.gridx = 1
    //gbc.gridy = 8
    //gbc.gridwidth = 2
    //gbc.gridwidth = GridBagConstraints.REMAINDER
    //gbc.fill = GridBagConstraints.HORIZONTAL
    //gbc.weightx = 1.0 // Add this line to make it expand horizontally
    //gbc.weighty = 1.0 // Add this line to make it expand vertically
    //fileManagerPanel.add(textToFindTextArea, gbc)

    gbc.gridx = 1
    gbc.gridy = 0
    gbc.gridwidth = 1
    fileManagerPanel.add(JLabel("Text to Insert:"), gbc)

    //gbc.gridx = 1
    //gbc.gridy = 9
    //gbc.gridwidth = 2
    //gbc.gridwidth = GridBagConstraints.REMAINDER
    //gbc.fill = GridBagConstraints.HORIZONTAL
    //gbc.weightx = 1.0 // Add this line to make it expand horizontally
    //gbc.weighty = 1.0 // Add this line to make it expand vertically
    //fileManagerPanel.add(textToInsertTextArea, gbc)

    //gbc.gridx = 0
    //    gbc.gridy = 11
    //    gbc.gridwidth = 1
    //    fileManagerPanel.add(JLabel("Backup Path:"), gbc)

        //gbc.gridx = 1
       // gbc.gridy = 11
        //gbc.gridwidth = 2
        //fileManagerPanel.add(backupPathChooser, gbc)

       // gbc.gridx = 0
        //gbc.gridy = 12
        //gbc.gridwidth = 1
        */

    tabbedPane.addTab("Text Editor", null, textEditorPanel, "Switch to Text Editor")
    tabbedPane.addTab("File Manager", null, fileManagerPanel, "Switch to File Manager")
    tabbedPane.addTab("Scrambler", null, scramblerPanel, "Switch to Scrambler")

    executeButton.addActionListener(ExecuteButtonClickListener())
    backupButton.addActionListener(BackupButtonClickListener())
    textMatchButton.addActionListener(TextMatchButtonClickListener())
    textInsertButton.addActionListener(TextInsertButtonClickListener())

    contentPane.add(tabbedPane)

    revalidate()
    repaint()
    isVisible = true
  }

    // Method to show the custom backup dialog
    private fun showBackupDialog() {
        val selectedOption = backupPathChooser.showSaveDialog(this)
        if (selectedOption == JFileChooser.APPROVE_OPTION) {
            val selectedFile = backupPathChooser.selectedFile
            // Implement backup logic using selectedFile
            if (selectedFile != null) {
                println("Backup Path: ${selectedFile.absolutePath}")
                // Implement your backup logic here
            }
        }
    }

    inner class ExecuteButtonClickListener : ActionListener {
        override fun actionPerformed(e: ActionEvent) {
            //val userInput = // Get user input from text fields or other components
            //val command = cmdFactory.createCommand(userInput)
            //execContext.run(command)
        }
    }

    inner class BackupButtonClickListener : ActionListener {
        override fun actionPerformed(e: ActionEvent) {}
    }

        inner class TextMatchButtonClickListener : ActionListener {
        override fun actionPerformed(e: ActionEvent) {}
    }

        inner class TextInsertButtonClickListener : ActionListener {
        override fun actionPerformed(e: ActionEvent) {}
    }
}