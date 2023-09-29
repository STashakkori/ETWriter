// ExecutionContext.kt
// $t@$h    QVLx Labs

package moneysaver

class ExecutionContext {
  fun run(command: Command): String {
    when (command.arguments.pathType) {
      PathType.SINGLE -> return executeSingle(command)
      PathType.DIRECTORY, PathType.RECURSIVE -> return executeDirectory(command)
      PathType.SPECIAL -> return executeFromTop(command)
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

  private fun executeDirectory(command: Command): String {
    val results = StringBuilder()

    if (!command.initialized) {
      ErrorManager.pushError(SeverityLevel.LOW, "Refusing uninitialized command.")
      return ""
    }
    command.validate()
    if (!command.valid) {
      ErrorManager.pushError(SeverityLevel.LOW, "Refusing invalid command.")
      return ""
    }
    if (command.arguments.pathType == PathType.DIRECTORY ||
        command.arguments.pathType == PathType.RECURSIVE) {
      command.directory.forEach {
      file ->
        command.arguments.setSourcePath(file)
        command.validate()
        if (!command.valid) {
          ErrorManager.pushError(SeverityLevel.LOW, "Refusing invalid command.")
          return ""
        }
        @Suppress("UNUSED_VARIABLE")
        val result = command.execute()
        results.append(result)
        command.valid = false
      }
    }
    return results.toString()
  }

  private fun executeFromTop(command: Command): String {
    val results = StringBuilder()

    if (!command.initialized) {
      ErrorManager.pushError(SeverityLevel.LOW, "Refusing uninitialized command.")
      return ""
    }
    command.validate()
    if (!command.valid) {
      ErrorManager.pushError(SeverityLevel.LOW, "Refusing invalid command.")
      return ""
    }
    val result = command.execute()
    results.append(result)
    command.valid = false
    return results.toString()
  }
}