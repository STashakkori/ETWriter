// CommandFactory.kt
// -$t@$h     QVLx Labs

package moneysaver

import java.io.File

class CommandFactory {
  fun determineCommandType(input: String): CommandType {
    return when (input) {
      "end" -> CommandType.END
      "quit" -> CommandType.QUIT
      "read_script" -> CommandType.READ_SCRIPT
      "find_append" -> CommandType.FIND_APPEND
      "find_replace" -> CommandType.FIND_REPLACE
      "find_delete" -> CommandType.FIND_DELETE
      "find_break" -> CommandType.FIND_BREAK
      "line_append" -> CommandType.LINE_APPEND
      "line_replace" -> CommandType.LINE_REPLACE
      "line_delete" -> CommandType.LINE_DELETE
      "line_break" -> CommandType.LINE_BREAK
      "save_file" -> CommandType.SAVE_FILE
      "load_file" -> CommandType.LOAD_FILE
      "tree_view" -> CommandType.TREE_VIEW
      else -> CommandType.UNKNOWN
    }
  }

  fun preprocessArgs(cmd: CommandType, args: Array<String>): Arguments {
    val command = cmd
    val sourcePath = File(args[1].trim())
    val maxSize = args.getOrNull(2)?.toLongOrNull() ?: 104857600
    val extension = args.getOrNull(3) ?: "" // Probably need to fix -ST
    val lineNumber = args.getOrNull(4)?.toIntOrNull() ?: -1
    val textToFind = args.getOrNull(5)
    val textToAdd = args.getOrNull(6)

    val pathType = if (sourcePath.exists() && sourcePath.canWrite()) {
      if (command == CommandType.TREE_VIEW) { PathType.SPECIAL }
      else if (sourcePath.isDirectory) {
        if (args[1].endsWith("/*")) PathType.RECURSIVE else PathType.DIRECTORY
      }
      else if (sourcePath.isFile) {
        if (args[1].endsWith(".et")) { PathType.INTERNAL }
        else if (args[1].endsWith(".docx")) { PathType.DOCX }
        else { PathType.SINGLE }
      }
      else { PathType.UNKNOWN }
    }
    else {
      ErrorManager.pushError(SeverityLevel.LOW, "Invalid path type given.")
      PathType.UNKNOWN
    }
    return Arguments(pathType, command, sourcePath, maxSize,
                     extension, lineNumber, textToFind, textToAdd)
  }

  fun createCommand(args: Array<String>): Command {
    val commandType = determineCommandType(args[0].trim())
    val arguments = preprocessArgs(commandType, args)

    val command = when (commandType) {
      CommandType.END -> EndCommand(arguments)
      CommandType.QUIT -> QuitCommand(arguments)
      CommandType.READ_SCRIPT -> ReadScriptCommand(arguments)
      CommandType.FIND_APPEND -> FindAppendCommand(arguments)
      CommandType.FIND_REPLACE -> FindReplaceCommand(arguments)
      CommandType.FIND_DELETE -> FindDeleteCommand(arguments)
      CommandType.FIND_BREAK -> FindBreakCommand(arguments)
      CommandType.LINE_APPEND -> LineAppendCommand(arguments)
      CommandType.LINE_REPLACE -> LineReplaceCommand(arguments)
      CommandType.LINE_DELETE -> LineDeleteCommand(arguments)
      CommandType.LINE_BREAK -> LineBreakCommand(arguments)
      CommandType.SAVE_FILE -> SaveFileCommand(arguments)
      CommandType.LOAD_FILE -> LoadFileCommand(arguments)
      CommandType.TREE_VIEW -> TreeViewCommand(arguments)
      CommandType.UNKNOWN -> { 
        ErrorManager.pushError(SeverityLevel.LOW, "Unknown command given.")
        EndCommand(arguments)
      }
    }
    initializeCommand(command)
    return command
  }

  fun initializeCommand(command: Command) {
    when (command.arguments.pathType) {
      PathType.SINGLE -> { command.initialized = true }
      PathType.DIRECTORY -> {
        var totalSize = 0L

        command.arguments.sourcePath.listFiles()?.forEach { 
          file ->
          if (file.isFile && file.canWrite() &&
              file.name.endsWith(command.arguments.extension)) {
            command.directory.add(file)
            totalSize += file.length()
          }
        }

        if (totalSize <= command.arguments.maxSize ||
            !command.directory.isEmpty()) { command.initialized = true }
        else {
          ErrorManager.pushError(SeverityLevel.LOW, "Total size exceeds maximum.")
        }
      }
      PathType.RECURSIVE -> {
        var totalSize = 0L

        command.arguments.sourcePath.walk().forEach {
          file ->
          if (file.isFile && file.canWrite() &&
              file.name.endsWith(command.arguments.extension)) {
            command.directory.add(file)
            totalSize += file.length()
          }
        }

        if (totalSize <= command.arguments.maxSize ||
            !command.directory.isEmpty()) { command.initialized = true }
        else {
          ErrorManager.pushError(SeverityLevel.LOW, "Total size exceeds maximum.")
        }
      }
      PathType.SPECIAL -> {
        command.initialized = true
      }
      PathType.INTERNAL -> {
        // Not supported right now
      }
      PathType.DOCX -> {
        // Not supported right now
      }
      PathType.UNKNOWN -> {
        ErrorManager.pushError(SeverityLevel.MEDIUM, "Unknown path type given to init.")
      }
    }
  }

  fun extractExtension(extensionArg: String): String {
    return extensionArg.substringAfterLast(".", "")
  }
}