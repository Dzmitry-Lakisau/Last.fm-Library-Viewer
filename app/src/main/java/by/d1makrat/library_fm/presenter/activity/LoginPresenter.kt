package by.d1makrat.library_fm.presenter.activity

import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.asynctask.GetSessionKeyAsyncTask
import by.d1makrat.library_fm.asynctask.GetSessionKeyCallback
import by.d1makrat.library_fm.asynctask.GetUserInfoAsyncTask
import by.d1makrat.library_fm.asynctask.GetUserInfoCallback
import by.d1makrat.library_fm.https.HttpsClient
import by.d1makrat.library_fm.model.User
import by.d1makrat.library_fm.view.activity.LoginView
import java.lang.Exception

class LoginPresenter:  GetSessionKeyCallback, GetUserInfoCallback {

    private var view: LoginView? = null

    fun attachView(view: LoginView){
        this.view = view
    }

    fun detachView(){
        view = null
    }

    fun onEnterInApp(){
        if (AppContext.getInstance().sessionKey != null && AppContext.getInstance().user != null)
            view?.startMainActivity()
        else
            view?.createListeners()
    }

    fun onEnterButtonClick(username: String, password: String) {
        if (HttpsClient.isNetworkAvailable()) {
            val getSessionKeyAsyncTask = GetSessionKeyAsyncTask(this)
            getSessionKeyAsyncTask.execute(username, password)

            view?.showProgressBar()
        }
        else {
            view?.showNoConnectionMessage()
        }
    }

    override fun onSessionKeyGranted(sessionKey: String?) {
        AppContext.getInstance().sessionKey = sessionKey
        AppContext.getInstance().saveSettings()

        val getUserInfoAsyncTask = GetUserInfoAsyncTask(this)
        getUserInfoAsyncTask.execute()
    }

    override fun onException(exception: Exception) {
        view?.hideProgressBar()
        view?.showErrorMessage(exception.message!!)
    }

    override fun onUserInfoReceived(user: User?) {
        AppContext.getInstance().user = user
        AppContext.getInstance().saveSettings()

        view?.startMainActivity()
    }
}
