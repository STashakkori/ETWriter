// LineReplaceCommand.kt
// By: Sina Tashakkori, QVLx Labs

package moneysaver

import java.io.File

class LineReplaceCommand(override val arguments: Arguments) : Command {
  override var initialized: Boolean = false
  override var valid: Boolean = false
  override val batch: MutableList<Command> = mutableListOf()
  override val directory: MutableList<File> = mutableListOf()

  override fun validate() {
    if (arguments.lineNumber >= 0 &&
        arguments.textToAdd != null) { valid = true }
    if (arguments.sourcePath.isFile) {
      if (arguments.lineNumber >= arguments.sourcePath.length()) {
        valid = false
      }
    }
  }

  override fun execute(): String {
    val file = arguments.sourcePath
    val lines = file.readLines().toMutableList()
    val textToInsert = arguments.textToAdd ?: ""

    lines[arguments.lineNumber] = textToInsert
    file.writeText(lines.joinToString("\n"))

    return ""
  }
}