// TextEditorTab.kt
package moneysaver

import javax.swing.*
import java.awt.*
import java.awt.event.*
import javax.swing.event.*
import javax.swing.text.*
import javax.swing.undo.*

class TextEditorTab(): JPanel() {
  internal val textArea = JTextArea()
  internal val scrollPane = JScrollPane(textArea)
  private val gotoButton: JButton = JButton("Jump")
  internal val statusField: JTextField = JTextField("STATUS")
  private val spinnerModel = SpinnerNumberModel(0, 0, 999999, 1)
  private val lineNumberSpinner = JSpinner(spinnerModel)
  internal val pathField: JTextField = JTextField("USE MANAGER TO OPEN FILE")
  internal val saveButton: JButton = JButton("Save")
  internal val closeButton: JButton = JButton("Close")
  private val undoManager = UndoManager()
  private var previousSpinnerValue = 0
  private inner class UndoAction : AbstractAction("Undo") {
    override fun actionPerformed(e: ActionEvent) {
      if (undoManager.canUndo()) {
        try { undoManager.undo() }
        catch (ex: CannotUndoException) { ex.printStackTrace() }
      }
    }
  }
  private inner class RedoAction : AbstractAction("Redo") {
    override fun actionPerformed(e: ActionEvent) {
      if (undoManager.canRedo()) {
        try { undoManager.redo() }
        catch (ex: CannotRedoException) { ex.printStackTrace() }
      }
    }
  }

  init {
    layout = GridBagLayout()
    val textEditorGbc = GridBagConstraints()
    textEditorGbc.insets = Insets(5, 5, 5, 5)
    textArea.selectionColor = Color(192, 192, 192) // Settings tab
    textArea.selectedTextColor = Color.BLACK

    val buttonPanel = JPanel(GridBagLayout())

    textEditorGbc.gridx = 0 // Row
    textEditorGbc.gridy = 0 // Column
    textEditorGbc.gridwidth = 1
    textEditorGbc.anchor = GridBagConstraints.LINE_START
    lineNumberSpinner.preferredSize = Dimension(100,
                                lineNumberSpinner.preferredSize.height)
    spinnerModel.maximum = 99999999
    buttonPanel.add(lineNumberSpinner, textEditorGbc)

    textEditorGbc.gridx = 1
    pathField.isEditable = false
    pathField.horizontalAlignment = JTextField.CENTER
    textEditorGbc.fill = GridBagConstraints.HORIZONTAL
    pathField.preferredSize = Dimension(340,
                                  pathField.preferredSize.height)
    
    buttonPanel.add(pathField, textEditorGbc)

    textEditorGbc.gridx = 2
    buttonPanel.add(saveButton, textEditorGbc)

    textEditorGbc.gridx = 3
    buttonPanel.add(closeButton, textEditorGbc)

    textEditorGbc.gridx = 4
    statusField.isEditable = false
    statusField.horizontalAlignment = JTextField.CENTER
    statusField.preferredSize = Dimension(100,
                                statusField.preferredSize.height)
    buttonPanel.add(statusField, textEditorGbc)

    saveButton.addActionListener {
      val confirmation = JOptionPane.showConfirmDialog(
        this,
        "Overwrite File? Yes or No",
        "Confirmation",
        JOptionPane.YES_NO_OPTION
      )

      if (confirmation == JOptionPane.YES_OPTION) {
        // Save file
      }
    }

    add(buttonPanel, textEditorGbc)

    textArea.document.addUndoableEditListener { e ->
      if (e.edit.presentationName != null) {
        undoManager.addEdit(e.edit)
      }
    }

    // Create actions for copy, paste, and undo
    val copyAction = DefaultEditorKit.CopyAction()
    val pasteAction = DefaultEditorKit.PasteAction()
    val undoAction = UndoAction()
    val redoAction = RedoAction()
    val cutAction = DefaultEditorKit.CutAction()

    val inputMap = textArea.getInputMap(JTextComponent.WHEN_FOCUSED)
    val actionMap = textArea.getActionMap()

    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK), "cutAction")
    actionMap.put("cutAction", cutAction)

    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK), "copyAction")
    actionMap.put("copyAction", copyAction)

    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK), "pasteAction")
    actionMap.put("pasteAction", pasteAction)

    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK), "undoAction")
    actionMap.put("undoAction", undoAction)

    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK), "redoAction")
    actionMap.put("redoAction", redoAction)

    val caretListener = CaretListener { e ->
      try {
        val dot = e.dot
        val mark = e.mark

        if (dot != mark) { // Multi
          spinnerModel.value = previousSpinnerValue
        } else { // Uni
          textArea.highlighter.removeAllHighlights()
          val lineNumber = textArea.getLineOfOffset(dot)
          previousSpinnerValue = (spinnerModel.value as Int) // Store the previous value as Int
          spinnerModel.value = lineNumber
          val lightGray = Color(192, 192, 192)
          val highlightPainter = DefaultHighlighter.DefaultHighlightPainter(lightGray)
          val highlighter = textArea.highlighter
          highlighter.addHighlight(
            textArea.getLineStartOffset(lineNumber),
            textArea.getLineEndOffset(lineNumber),
            highlightPainter
          )
          textArea.repaint()
        }
      } catch (ex: BadLocationException) {
          ex.printStackTrace()
      }
    }

    lineNumberSpinner.addChangeListener(object : ChangeListener {
      override fun stateChanged(e: ChangeEvent) {
        // Get the selected line number from the spinner and update the caret position
        val selectedLineNumber = spinnerModel.number.toInt()
        val dot = textArea.getLineStartOffset(selectedLineNumber)
        textArea.caretPosition = dot
      }
    })

    val mouseListener = object : MouseAdapter() {
      override fun mouseClicked(e: MouseEvent) {
        caretListener.caretUpdate(null)
      }
    }

    textArea.addCaretListener(caretListener)
    textArea.addMouseListener(mouseListener)

    textArea.addAncestorListener(object : AncestorListener {
      override fun ancestorAdded(e: AncestorEvent?) {
        SwingUtilities.invokeLater { textArea.requestFocusInWindow() }
      }
      override fun ancestorMoved(e: AncestorEvent?) {}
      override fun ancestorRemoved(e: AncestorEvent?) {}
    })

    textEditorGbc.gridx = 0
    textEditorGbc.gridy = 1
    textEditorGbc.gridwidth = GridBagConstraints.REMAINDER
    textEditorGbc.weightx = 1.0
    textEditorGbc.weighty = 1.0
    textEditorGbc.fill = GridBagConstraints.BOTH
    add(scrollPane, textEditorGbc)
  }

  fun loadOutputIntoTextArea(output: String) {
    textArea.text = output
  }

  inner class LineNumberSpinnerListener : ChangeListener {
    override fun stateChanged(e: ChangeEvent) {
      // Get the selected line number from the spinner and update the caret position
      val selectedLineNumber = spinnerModel.number.toInt()
      val dot = textArea.getLineStartOffset(selectedLineNumber)
      textArea.caretPosition = dot
    }
  }

  fun updatePathField(filePath: String) {
    pathField.text = filePath
  }

  fun setStatus(status: String, backgroundColor: Color?) {
    statusField.text = status
    statusField.background = backgroundColor
  }

  private fun moveCaretLeftByLine() {
    val caret = textArea.caret
    val dot = caret.dot
    val lineNumber = textArea.getLineOfOffset(dot)
    if (lineNumber > 0) {
      caret.setDot(textArea.getLineStartOffset(lineNumber - 1))
      caret.moveDot(textArea.getLineEndOffset(lineNumber - 1))
    }
  }

  private fun moveCaretRightByLine() {
    val caret = textArea.caret
    val dot = caret.dot
    val lineNumber = textArea.getLineOfOffset(dot)
    val lineCount = textArea.lineCount
    if (lineNumber < lineCount - 1) {
      caret.setDot(textArea.getLineStartOffset(lineNumber + 1))
      caret.moveDot(textArea.getLineEndOffset(lineNumber + 1))
    }
  }
}