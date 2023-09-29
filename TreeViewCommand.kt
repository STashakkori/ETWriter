package moneysaver

import java.io.File
import java.io.StringWriter
import java.nio.file.Files
import java.nio.file.Path

class TreeViewCommand(override val arguments: Arguments) : Command {
  override var initialized: Boolean = false
  override var valid: Boolean = false
  override val batch: MutableList<Command> = mutableListOf()
  override val directory: MutableList<File> = mutableListOf()

  override fun validate() {
    valid = true
  }

  override fun execute(): String {
    val generator = DirectoryTreeGenerator()
    val directoryTree = generator.generateDirectoryTree(arguments.sourcePath)
    return directoryTree
  }
}

class DirectoryTreeGenerator {
  private fun writeDirectoryTree(directory: File, writer: StringWriter, indentLevel: Int = 0) {
    if (directory.isDirectory) {
      writer.append("${"  ".repeat(indentLevel)}[D] ${directory.name}\n")
      directory.listFiles()?.forEach { entry ->
        try {
          writeDirectoryTree(entry, writer, indentLevel + 1)
        } catch (e: Exception) {
          writer.append("${"  ".repeat(indentLevel + 1)}[O] ${entry.name} (Error: ${e.message})\n")
        }
      }
    } else if (directory.isFile) {
      writer.append("${"  ".repeat(indentLevel)}[F] ${directory.name}\n")
    } else if (Files.isSymbolicLink(Path.of(directory.toURI()))) {
      writer.append("${"  ".repeat(indentLevel)}[S] ${directory.name}\n")
    } else {
      writer.append("${"  ".repeat(indentLevel)}[O] ${directory.name}\n")
    }
  }

    fun generateDirectoryTree(path: File): String {
      val writer = StringWriter()
      try {
        writeDirectoryTree(path, writer)
      } catch (e: Exception) {
        writer.append("[O] ${path.name} (Error: ${e.message})\n")
      }
      return writer.toString()
  }
}