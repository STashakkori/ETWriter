// LoadFileCommand.kt

package moneysaver

import java.io.File
import java.io.IOException

class LoadFileCommand(override val arguments: Arguments) : Command {
  override var initialized: Boolean = false
  override var valid: Boolean = false
  override val batch: MutableList<Command> = mutableListOf()
  override val directory: MutableList<File> = mutableListOf()

  override fun validate() {
    if (arguments.pathType == PathType.SINGLE) { valid = true }
  }

  override fun execute(): String {
    try {
      return arguments.sourcePath.readText()
    }
    catch (e: IOException) {
      ErrorManager.pushError(SeverityLevel.LOW,
                  "Error loading ${arguments.sourcePath}: ${e.message}")
      return ""
    }
  }
}