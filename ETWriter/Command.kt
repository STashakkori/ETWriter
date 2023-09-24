// Command.kt
// -$t@$h    QVLx Labs

package moneysaver

import java.io.File

data class Arguments(
  val pathType: PathType, // Non-arg. Gets set for internal use
  val command: CommandType, // Command to perform on file or folder
  private var _sourcePath: File, // Path to perform command on. A file or folder
  val maxSize: Long, // Max number of bytes that can be performed on.
  val extension: String, // File extension to perform on. Optional
  val lineNumber: Int, // Line number in files to perform on. Optional
  val textToFind: String?, // String to find. Optional for command.
  val textToInsert: String? // Insertion payload. Optional for command
) {
  val sourcePath: File
  get() = _sourcePath
  internal fun setSourcePath(value: File) {
   _sourcePath = value
  }
}

enum class PathType {
  SINGLE,
  DIRECTORY,
  RECURSIVE,
  DOCX,
  UNKNOWN
}

enum class CommandType {
  END,
  QUIT,
  
  SAVE_FILE,
  LOAD_FILE,

  READ_SCRIPT,

  FIND_INSERT,
  FIND_REPLACE,
  FIND_DELETE,

  FIND_BREAK,
  LINE_INSERT,
  LINE_REPLACE,
  LINE_DELETE,
  LINE_BREAK,

  UNKNOWN
}

interface Command {
  var initialized: Boolean
  var valid: Boolean
  val arguments: Arguments
  val batch: MutableList<Command>
  val directory: MutableList<File>

  fun validate()
  fun execute(): String
}
