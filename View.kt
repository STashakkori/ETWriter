// $@$h, QVLx Labs

package moneysaver

interface View {
    fun displayMessage(message: String)
    fun displayError(error: String)
    fun displayAllErrors()
}