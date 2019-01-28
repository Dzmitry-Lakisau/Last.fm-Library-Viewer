package by.d1makrat.library_fm.presenter.activity

import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.model.User
import by.d1makrat.library_fm.utils.ConnectionChecker
import by.d1makrat.library_fm.view.activity.MainView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainPresenter {

    private var view: MainView? = null
    private val compositeDisposable = CompositeDisposable()

    private var user = AppContext.getInstance().user

    fun attachView(view: MainView){
        this.view = view
        view.setUserInfoInHeader(user)
    }

    fun detachView(){
        view = null
        compositeDisposable.clear()

        settings.save()
    }

    fun onNavigationItemSelected(){
        if (ConnectionChecker.isNetworkAvailable()) {
            compositeDisposable.add(
                    AppContext.getInstance().retrofitWebService.getUserInfo(AppContext.getInstance().user.username)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    {
                                        onUserInfoReceived(it)
                                    },
                                    {})
            )
        }
    }

    fun onLogout(){
        this.user = null
        settings.sessionKey.key = null

        compositeDisposable.add(
                AppContext.getInstance().repository.clearDatabase()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe()
        )
    }

    private fun onUserInfoReceived(user: User) {
        this.user = user
        view?.setUserInfoInHeader(user)
    }
}
