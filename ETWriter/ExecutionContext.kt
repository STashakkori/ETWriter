// ExecutionContext.kt
// -$t@$h  QVLx Labs

package moneysaver

class ExecutionContext {
  fun run(command: Command): String {
    when (command.arguments.pathType) {
      PathType.SINGLE -> return executeSingle(command)
      PathType.DIRECTORY, PathType.RECURSIVE -> executeDirectory(command)
      else -> {}
    }
    return ""
  }

  private fun executeSingle(command: Command): String {
    if (!command.initialized) {
      ErrorManager.pushError(SeverityLevel.LOW, "Refusing uninitialized command.")
      return ""
    }
    command.validate()
    if (!command.valid) {
      ErrorManager.pushError(SeverityLevel.LOW, "Refusing invalid command.")
      return ""
    }
    return command.execute()
  }

  private fun executeDirectory(command: Command) {
    if (!command.initialized) {
      ErrorManager.pushError(SeverityLevel.LOW, "Refusing uninitialized command.")
      return
    }
    command.validate()
    if (!command.valid) {
      ErrorManager.pushError(SeverityLevel.LOW, "Refusing invalid command.")
      return
    }
    if (command.arguments.pathType == PathType.DIRECTORY ||
        command.arguments.pathType == PathType.RECURSIVE) {
      command.directory.forEach {
      file ->
        command.arguments.setSourcePath(file)
        command.validate()
        if (!command.valid) {
          ErrorManager.pushError(SeverityLevel.LOW, "Refusing invalid command.")
          return
        }
        @Suppress("UNUSED_VARIABLE")
        val result = command.execute()
        command.valid = false
      }
    }
  }
}