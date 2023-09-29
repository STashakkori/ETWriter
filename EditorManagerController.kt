// EditorManagerController.kt
package moneysaver

import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class EditorManagerController(val fileManagerTab: FileManagerTab,
                              val textEditorTab: TextEditorTab,
                              val cmdFactory: CommandFactory,
                              val execContext: ExecutionContext) {

  private var loadedPath: String = ""
  private var filePath: String = ""

  init {
    fileManagerTab.sourcePathChooser.addActionListener { _ ->
      filePath = fileManagerTab.sourcePathChooser.selectedFile.name
      val filePathAbs = fileManagerTab.sourcePathChooser.selectedFile.absolutePath
      textEditorTab.updatePathField(filePathAbs)
      val cmd = cmdFactory.createCommand(arrayOf("load_file", filePathAbs))
      val output = execContext.run(cmd)
      textEditorTab.loadOutputIntoTextArea(output)
      fileManagerTab.setStatus("Loaded $filePath", Color.GREEN)
      textEditorTab.setStatus("Loaded", Color.GREEN)
      setLoadedPath(filePathAbs)
    }

    val executeButtonClickListener = ExecuteButtonClickListener()
    val backupButtonClickListener = BackupButtonClickListener()
    val encryptButtonClickListener = EncryptButtonClickListener()
    val decryptButtonClickListener = DecryptButtonClickListener()
    val hashButtonClickListener = HashButtonClickListener()
    val textMatchButtonClickListener = TextMatchButtonClickListener()
    val textInsertButtonClickListener = TextInsertButtonClickListener()

    fileManagerTab.executeButton.addActionListener(executeButtonClickListener)
    fileManagerTab.backupButton.addActionListener(backupButtonClickListener)
    fileManagerTab.encryptButton.addActionListener(encryptButtonClickListener)
    fileManagerTab.decryptButton.addActionListener(decryptButtonClickListener)
    fileManagerTab.hashButton.addActionListener(hashButtonClickListener)
    fileManagerTab.textMatchButton.addActionListener(textMatchButtonClickListener)
    fileManagerTab.textInsertButton.addActionListener(textInsertButtonClickListener)

    textEditorTab.closeButton.addActionListener {
      setLoadedPath("")
      textEditorTab.loadOutputIntoTextArea("")
      if (!filePath.isEmpty()) {
        textEditorTab.setStatus("Closed", Color.GREEN)
        textEditorTab.updatePathField("USE FILE MANAGER TO OPEN FILE")
        fileManagerTab.setStatus("$filePath closed", Color.GREEN)
      }
      else {
        textEditorTab.setStatus("STATUS", textEditorTab.pathField.background)
        fileManagerTab.setStatus("STATUS", textEditorTab.pathField.background)
      } 
    }
  }

  inner class ExecuteButtonClickListener : ActionListener {
    override fun actionPerformed(e: ActionEvent) {
    //val userInput = // Get user input from text fields or other components
    //val command = cmdFactory.createCommand(userInput)
    //execContext.run(command)
    }
  }
  inner class BackupButtonClickListener : ActionListener {
    override fun actionPerformed(e: ActionEvent) {}
  }

  inner class EncryptButtonClickListener : ActionListener {
    override fun actionPerformed(e: ActionEvent) {}
  }
  
  inner class DecryptButtonClickListener : ActionListener {
    override fun actionPerformed(e: ActionEvent) {}
  }

  inner class HashButtonClickListener : ActionListener {
    override fun actionPerformed(e: ActionEvent) {}
  }

  inner class TextMatchButtonClickListener : ActionListener {
    override fun actionPerformed(e: ActionEvent) {}
  }

  inner class TextInsertButtonClickListener : ActionListener {
    override fun actionPerformed(e: ActionEvent) {}
  }

  fun setLoadedPath(path: String) {
    loadedPath = path
  }
}