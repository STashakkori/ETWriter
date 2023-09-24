// EndCommand.kt
// -$t@$h   QVLx Labs

package moneysaver

import java.io.File

class EndCommand(override val arguments: Arguments) : Command {
  override var initialized: Boolean = false
  override var valid: Boolean = false
  override val batch: MutableList<Command> = mutableListOf()
  override val directory: MutableList<File> = mutableListOf()

  override fun validate() {}

  override fun execute(): String { return "" }
}