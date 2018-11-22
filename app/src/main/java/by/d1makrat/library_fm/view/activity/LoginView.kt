package by.d1makrat.library_fm.view.activity

interface LoginView {
    fun createListeners()
    fun hideProgressBar()
    fun showErrorMessage(message: String)
    fun showNoConnectionMessage()
    fun showProgressBar()
    fun startMainActivity()
}
