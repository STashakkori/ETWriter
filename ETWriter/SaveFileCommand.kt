// SaveFileCommand.kt

package moneysaver

import java.io.File
import java.io.IOException

class SaveFileCommand(override val arguments: Arguments) : Command {
  override var initialized: Boolean = false
  override var valid: Boolean = false
  override val batch: MutableList<Command> = mutableListOf()
  override val directory: MutableList<File> = mutableListOf()

  override fun validate() {
    if (arguments.textToInsert != null) { valid = true }
  }

  override fun execute(): String {
    try {
      val textToSave = arguments.textToInsert ?: ""
      arguments.sourcePath.writeText(textToSave)
    } catch (e: IOException) {
      ErrorManager.pushError(SeverityLevel.LOW,
                        "Error saving to $arguments.sourcePath: ${e.message}")
    }
    return ""
  }
}