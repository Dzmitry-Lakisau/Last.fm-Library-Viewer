package by.d1makrat.library_fm.presenter.activity

import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.utils.ConnectionChecker
import by.d1makrat.library_fm.json.SessionKeyAdapter
import by.d1makrat.library_fm.model.SessionKey
import by.d1makrat.library_fm.model.User
import by.d1makrat.library_fm.utils.ExceptionHandler
import by.d1makrat.library_fm.view.activity.LoginView
import com.google.gson.GsonBuilder
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginPresenter {

    private var view: LoginView? = null
    private val compositeDisposable = CompositeDisposable()

    fun attachView(view: LoginView){
        this.view = view
    }

    fun detachView(){
        view = null
        compositeDisposable.clear()
    }

    fun onEnterInApp(){
        if (AppContext.getInstance().sessionKey != null && AppContext.getInstance().user != null)
            view?.startMainActivity()
        else
            view?.createListeners()
    }

    fun onEnterButtonClick(username: String, password: String) {
        if (ConnectionChecker.isNetworkAvailable()) {
            compositeDisposable.add(
                    Single.create<SessionKey> { singleEmitter ->
                        try {
                            val response = AppContext.getInstance().retrofitWebService.getSessionKey(username, password).execute()

                            if (response.isSuccessful){
                                singleEmitter.onSuccess(response.body()!!)
                            }
                            else {
                                GsonBuilder().registerTypeAdapter(SessionKey::class.java, SessionKeyAdapter())
                                        .create().fromJson(response.errorBody()!!.string(), SessionKey::class.java)
                            }
                        }
                        catch (e: Exception){
                            if (!singleEmitter.isDisposed) {
                                singleEmitter.onError(e)
                            }
                        }
                    }
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    {
                                        onSessionKeyGranted(it.key, username)
                                    },
                                    {
                                        onException(it)
                                    }
                            )
            )

            view?.showProgressBar()
        }
        else {
            view?.showNoConnectionMessage()
        }
    }

    private fun onSessionKeyGranted(sessionKey: String, username: String) {
        AppContext.getInstance().sessionKey = sessionKey
        AppContext.getInstance().saveSettings()

        compositeDisposable.add(
                AppContext.getInstance().retrofitWebService.getUserInfo(username)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {
                                    onUserInfoReceived(it)
                                },
                                {
                                    onException(it)
                                }
                        )
        )
    }

    private fun onUserInfoReceived(user: User) {
        AppContext.getInstance().user = user
        AppContext.getInstance().saveSettings()

        view?.startMainActivity()
    }

    private fun onException(exception: Throwable) {
        view?.hideProgressBar()
        view?.showErrorMessage(ExceptionHandler().sendExceptionAndGetReadableMessage(exception))
    }
}
