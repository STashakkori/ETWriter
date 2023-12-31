// FindReplaceCommand.kt

package moneysaver

import java.io.File

class FindReplaceCommand(override val arguments: Arguments) : Command {
  override var initialized: Boolean = false
  override var valid: Boolean = false
  override val batch: MutableList<Command> = mutableListOf()
  override val directory: MutableList<File> = mutableListOf()

  override fun validate() {
    if (arguments.textToFind != null &&
        arguments.textToAdd != null) { valid = true }
  }

  override fun execute(): String {
    val lines = arguments.sourcePath.readLines().toMutableList()
    val textToFind = arguments.textToFind ?: ""
    val textToAdd = arguments.textToAdd ?: ""
    var lineCounter = 0

    for (line in lines.indices) {
      if (lines[line].contains(textToFind)) {
        lines[line] = textToAdd
        lineCounter++
        if (lineCounter == arguments.lineNumber) break
      }
    }

    arguments.sourcePath.writeText(lines.joinToString("\n"))
    return ""
  }
}