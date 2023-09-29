// Error.kt
// -$sta$h QVLx Labs

package moneysaver

import java.util.ArrayList

enum class SeverityLevel {
  LOW,
  MEDIUM,
  HIGH
}

data class Error(val severity: SeverityLevel, val message: String)

object ErrorManager {
  private val errors = ArrayList<Error>()
  
  fun getErrors(): List<Error> {
    return errors
  }
  
  fun pushError(severity: SeverityLevel, message: String) {
    errors.add(Error(severity, message))
  }

  fun isEmpty(): Boolean {
    return errors.isEmpty()
  }

  fun size(): Int {
    return errors.size
  }

  fun clearErrors() {
    errors.clear()
  }
}