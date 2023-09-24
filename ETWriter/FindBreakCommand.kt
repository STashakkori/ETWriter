// FindBreakCommand.kt

package moneysaver

import java.io.File

class FindBreakCommand(override val arguments: Arguments) : Command {
  override var initialized: Boolean = false
  override var valid: Boolean = false
  override val batch: MutableList<Command> = mutableListOf()
  override val directory: MutableList<File> = mutableListOf()

  override fun validate() {
    if (arguments.textToFind != null) { valid = true }
  }

  override fun execute(): String {
    val lines = arguments.sourcePath.readLines().toMutableList()
    val textToFind = arguments.textToFind ?: ""
    var lineCounter = 0

    for (line in lines.indices) {
      if (lines[line].contains(textToFind)) {
        lines.add(line + 1, "")
        lineCounter++
        if (lineCounter == arguments.lineNumber) break
      }
    }

    arguments.sourcePath.writeText(lines.joinToString("\n"))
    return ""
  }
}