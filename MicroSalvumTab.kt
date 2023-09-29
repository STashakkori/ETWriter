// MicroSalvumTab.kt
package moneysaver

import javax.swing.*
import java.awt.*

class MicroSalvumTab(private val cmdFactory: CommandFactory,
                     private val execContext: ExecutionContext) : JPanel() {
  init {
    layout = GridBagLayout()
    val gbc = GridBagConstraints()
    val microSalvumShell = MicroSalvumController(cmdFactory, execContext)

    gbc.gridx = 0
    gbc.gridy = 4
    gbc.gridwidth = GridBagConstraints.REMAINDER
    gbc.fill = GridBagConstraints.BOTH
    gbc.weightx = 1.0
    gbc.weighty = 1.0
    add(microSalvumShell, gbc)
  }
}