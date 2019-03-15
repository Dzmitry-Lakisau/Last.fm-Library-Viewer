package by.d1makrat.library_fm.presenter.activity

import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.Constants.MAX_FOR_SCROBBLES_BY_ARTIST
import by.d1makrat.library_fm.image_loader.Malevich
import by.d1makrat.library_fm.view.activity.PreferenceView
import com.crashlytics.android.Crashlytics
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException

class PreferencePresenter {

    private val MAX_ITEMS_PER_REQUEST = 1000

    private var view: PreferenceView? = null
    private val compositeDisposable = CompositeDisposable()

    fun attachView(view: PreferenceView){
        this.view = view
    }

    fun detachView(){
        view = null
        compositeDisposable.clear()
    }

    fun onSetLimitButtonClicked(input: String) {
        try {
            val limit = input.toInt()
            if (limit in 1..MAX_ITEMS_PER_REQUEST) {
                if (limit > MAX_FOR_SCROBBLES_BY_ARTIST) {
                    view?.showLimitMoreThan200Message()
                }
                AppContext.getInstance().setLimit(limit.toString())
                view?.showLimitSetOKMessage()
            } else {
                view?.showLimitMustBeBetween()
            }
        } catch (exception: NumberFormatException){
            exception.printStackTrace()
            Crashlytics.logException(exception)
            view?.showNonnumericalInputMessage()
        }
    }

    fun onClearImageCacheButtonClicked() {
        try {
            Malevich.INSTANCE.clearCache()
            view?.showOK()
        } catch (exception: IOException) {
            exception.printStackTrace()
            Crashlytics.logException(exception)
            view?.showUnableToClearImageCache()
        }
    }

    fun onDropDatabaseButtonClicked() {
        compositeDisposable.add(
                AppContext.getInstance().repository.clearDatabase()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {
                                    view?.showOK()
                                },
                                {
                                    view?.showUnableToDropDatabaseMessage()
                                }
                        )
        )
    }
}
