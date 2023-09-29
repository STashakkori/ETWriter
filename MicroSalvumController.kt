package moneysaver

import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.*
import javax.swing.text.*
import javax.swing.text.DefaultStyledDocument
import javax.swing.text.BadLocationException

class MicroSalvumController(private val cmdFactory: CommandFactory,
                            private val execContext: ExecutionContext) : JPanel() {
  private val shellPanel: JPanel = JPanel()
  private val promptLabel: JLabel = JLabel("microSalvum> ")
  private val inputTextField: JTextField = JTextField(40)
  private val outputTextPane: JTextPane = JTextPane()
  private val doc: DefaultStyledDocument = DefaultStyledDocument()

  init {
    layout = BorderLayout()
    SwingUtilities.invokeLater {
      outputTextPane.isEditable = false
    }
    
    outputTextPane.font = Font("Monospaced", Font.PLAIN, 12)

    val scrollPane = JScrollPane(outputTextPane)
    scrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
    add(scrollPane, BorderLayout.CENTER)

    val promptInputPanel = JPanel()
    promptInputPanel.layout = BorderLayout()
    promptInputPanel.add(promptLabel, BorderLayout.WEST)
    promptInputPanel.add(inputTextField, BorderLayout.CENTER)

    shellPanel.add(promptInputPanel, BorderLayout.SOUTH)
    shellPanel.layout = BorderLayout()
    shellPanel.add(promptInputPanel, BorderLayout.CENTER)

    inputTextField.addActionListener(ActionListener {
      val command = inputTextField.text
      processCommand(command)
      inputTextField.text = ""
    })

    add(shellPanel, BorderLayout.SOUTH)

    addAncestorListener(object : javax.swing.event.AncestorListener {
      override fun ancestorAdded(e: javax.swing.event.AncestorEvent?) {
        SwingUtilities.invokeLater { inputTextField.requestFocusInWindow() }
      }

      override fun ancestorMoved(e: javax.swing.event.AncestorEvent?) {}
      override fun ancestorRemoved(e: javax.swing.event.AncestorEvent?) {}
    })

    outputTextPane.document = doc
  }

  private fun appendColoredText(text: String, color: Color) {
    val attr = SimpleAttributeSet()
    StyleConstants.setForeground(attr, color)
    try {
      doc.insertString(doc.length, text, attr)
    } catch (e: BadLocationException) {
      e.printStackTrace()
    }
  }

  private fun validateCommand(tokens: Array<String>): Boolean {
    if (tokens[0] in setOf("ls",
                           "dir",
                           "tree",
                           "cat",
                           "type",
                           "nop")) { return true } else { return false }
  }

  private fun processCommand(command: String) {
    val cmd_split = command.split(" ").toTypedArray()  
    val output = executeCommand(command, cmd_split)
    SwingUtilities.invokeLater {
      if(command.isBlank()) { appendColoredText(output, Color.BLUE) }
      else if(validateCommand(cmd_split)) { appendColoredText(output, Color.BLACK) }
      else { appendColoredText(output, Color.GRAY) }
    }
  }

  private fun executeCommand(command: String,
                             cmd_line: Array<String>): String {
    val default = "microSalvum> $command\n"
    if (command.isEmpty() || cmd_line.isEmpty()) { return default }
    val validArgs = preprocessArgsCLI(cmd_line)
    if (validArgs.isEmpty()) { return default }
    val cmd = cmdFactory.createCommand(validArgs)
    val output = execContext.run(cmd)
    val outputNL = if (output.endsWith('\n')) output else output + "\n"
    return "microSalvum> $command\n$outputNL"
  }

  private fun preprocessArgsCLI(args: Array<String>): Array<String> {
      if (args.isEmpty()) {
        return emptyArray()
      }

      if (args[0] in setOf("tree")) {
        if (args.size > 1) {
          return arrayOf("tree_view", args[1])
         } else {
          return arrayOf("tree_view", ".")
        }
      }

      if (args[0] in setOf("cat", "type")) {
        return arrayOf("load_file", args[1])
      }

      if (args[0] == "nop") {
        return arrayOf("end", ".")
      }

      return emptyArray()
  }
}