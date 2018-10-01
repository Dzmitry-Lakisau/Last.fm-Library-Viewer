package by.d1makrat.library_fm.presenter.activity

import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.asynctask.GetUserInfoAsyncTask
import by.d1makrat.library_fm.asynctask.GetUserInfoCallback
import by.d1makrat.library_fm.https.HttpsClient
import by.d1makrat.library_fm.model.User
import by.d1makrat.library_fm.view.activity.MainView

class MainPresenter:  GetUserInfoCallback {

    private var view: MainView? = null

    private var user = AppContext.getInstance().user

    fun attachView(view: MainView){
        this.view = view
        view.setUserInfoInHeader(user)
    }

    fun detachView(){
        view = null
        AppContext.getInstance().saveSettings()
    }

    fun onNavigationItemSelected(){
        if (HttpsClient.isNetworkAvailable()) {
            val getUserInfoAsyncTask = GetUserInfoAsyncTask(this)
            getUserInfoAsyncTask.execute()
        }
    }

    fun onLogout(){
        AppContext.getInstance().user = null
        AppContext.getInstance().sessionKey = null
    }

    override fun onUserInfoReceived(user: User) {
        AppContext.getInstance().user = user
        view?.setUserInfoInHeader(user)
    }
}
