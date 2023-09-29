// FileManagerTab.kt
package moneysaver

import javax.swing.*
import java.awt.*
import java.awt.event.*
import javax.swing.event.*
import javax.swing.text.*
import javax.swing.undo.*

class FileManagerTab() : JPanel() {
  private val commandComboBox: JComboBox<String> = JComboBox(arrayOf("FIND_APPEND",
                                                                     "FIND_REPLACE",
                                                                     "FIND_INSERT",
                                                                     "FIND_REPLACE",
                                                                     "FIND_DELETE",
                                                                     "LINE_APPEND",
                                                                     "LINE_REPLACE",
                                                                     "LINE_INSERT",
                                                                     "LINE_REPLACE",
                                                                     "LINE_DELETE"))
  val statusField: JTextField = JTextField("STATUS")
  // Set Color here to gray
  private val lineNumberField: JTextField = JTextField()
  private val lineNumberLabel = JLabel("Line Number")
  internal val textMatchButton: JButton = JButton("Find")
  internal val textInsertButton: JButton = JButton("Edit")
  internal val executeButton: JButton = JButton("Execute")
  internal val backupButton: JButton = JButton("Backup")
  internal val cancelButton: JButton = JButton("Cancel")
  internal val encryptButton: JButton = JButton("Encrypt")
  internal val decryptButton: JButton = JButton("Decrypt")
  internal val hashButton: JButton = JButton("Hash")
  private val maxSizeField: JComboBox<String> = JComboBox(arrayOf("10MB max",
                                                                  "1MB max",
                                                                  "1KB max",
                                                                  "1GB max",
                                                                  "100KB max",
                                                                  "500KB max"))
  internal val sourcePathChooser: JFileChooser = JFileChooser()

  init {
    layout = GridBagLayout()
    val gbc = GridBagConstraints()
    gbc.insets = Insets(0,0,0,0)

    val splash = this.javaClass.getResource("/ETSplash.png")
    val imageIcon = ImageIcon(splash)
    val image = imageIcon.image.getScaledInstance(200, 100,
                                                  Image.SCALE_SMOOTH)
    val scaledImageIcon = ImageIcon(image)
    val label = JLabel(scaledImageIcon)
    gbc.gridx = 0 // Column
    gbc.gridy = 0 // Row
    gbc.anchor = GridBagConstraints.LINE_START
    gbc.gridwidth = 1
    add(label, gbc)

    gbc.gridx = 0
    gbc.gridy = 1
    add(encryptButton, gbc)

    gbc.gridx = 1
    add(commandComboBox, gbc)

    gbc.anchor = GridBagConstraints.LINE_END
    add(textMatchButton, gbc)

    gbc.gridx = 0
    gbc.gridy = 2
    gbc.anchor = GridBagConstraints.LINE_START
    add(decryptButton, gbc)

    gbc.gridx = 1
    add(maxSizeField, gbc)

    gbc.anchor = GridBagConstraints.LINE_END
    add(textInsertButton, gbc)

    gbc.gridx = 0
    gbc.gridy = 3
    gbc.anchor = GridBagConstraints.LINE_START
    add(hashButton, gbc)

    gbc.gridx = 1
    add(backupButton, gbc)

    gbc.anchor = GridBagConstraints.LINE_END
    add(executeButton, gbc)

    gbc.gridx = 0
    gbc.gridy = 5
    gbc.gridwidth = GridBagConstraints.HORIZONTAL
    add(sourcePathChooser, gbc)
    
    statusField.isEditable = false
    gbc.gridx = 1
    gbc.gridy = 0
    gbc.fill = GridBagConstraints.HORIZONTAL
    gbc.anchor = GridBagConstraints.PAGE_START
    statusField.horizontalAlignment = JTextField.CENTER
    add(statusField, gbc)

    val labelAndFieldPanel = JPanel(BorderLayout())
    labelAndFieldPanel.add(lineNumberLabel, BorderLayout.NORTH)
    labelAndFieldPanel.add(lineNumberField, BorderLayout.CENTER)

    gbc.anchor = GridBagConstraints.PAGE_END
    add(labelAndFieldPanel, gbc)
  }

  fun setStatus(statusText: String, backgroundColor: Color) {
      statusField.text = statusText
      statusField.background = backgroundColor
  }
}