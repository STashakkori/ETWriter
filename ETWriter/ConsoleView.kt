// ConsoleView.kt
// -$t@$h     QVLx Labs

package moneysaver

class ConsoleView : View {
  override fun displayMessage(message: String) {
    println(message)
  }

  override fun displayError(error: String) {
    System.err.println("Error: $error")
  }

  override fun displayAllErrors() {
    if (ErrorManager.isEmpty()) { return }
    for (error in ErrorManager.getErrors()) {
      val severity = when (error.severity) {
        SeverityLevel.LOW -> "Low"
        SeverityLevel.MEDIUM -> "Medium"
        SeverityLevel.HIGH -> "High"
      }
      displayError("$severity Severity: ${error.message}")
    }
  }
}