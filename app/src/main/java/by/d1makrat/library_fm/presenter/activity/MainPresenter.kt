package by.d1makrat.library_fm.presenter.activity

import android.content.BroadcastReceiver
import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.BuildConfig
import by.d1makrat.library_fm.asynctask.CheckNewVersionAsyncTask
import by.d1makrat.library_fm.asynctask.CheckNewVersionCallback
import by.d1makrat.library_fm.asynctask.GetUserInfoAsyncTask
import by.d1makrat.library_fm.asynctask.GetUserInfoCallback
import by.d1makrat.library_fm.https.HttpsClient
import by.d1makrat.library_fm.model.User
import by.d1makrat.library_fm.view.activity.MainView
import java.lang.Exception

class MainPresenter:  GetUserInfoCallback, CheckNewVersionCallback {

    protected var view: MainView? = null

    private var user = AppContext.getInstance().user



    fun attachView(view: MainView){
        this.view = view
        view.setUserInfoInHeader(user)

        val checkNewVersionAsyncTask = CheckNewVersionAsyncTask(this)
        checkNewVersionAsyncTask.execute()
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

    override fun onSuccess(latestVersion: Int) {
        if (BuildConfig.VERSION_CODE < latestVersion) {
            view?.showUpdateDialog()
        }
    }
}
