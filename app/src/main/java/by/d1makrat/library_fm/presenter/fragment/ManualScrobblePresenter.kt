package by.d1makrat.library_fm.presenter.fragment

import android.text.TextUtils
import by.d1makrat.library_fm.APIException
import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.model.SendScrobbleResult
import by.d1makrat.library_fm.utils.ConnectionChecker
import by.d1makrat.library_fm.utils.ExceptionHandler
import by.d1makrat.library_fm.view.fragment.ManualScrobbleView
import com.crashlytics.android.Crashlytics
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

class ManualScrobblePresenter {

    private var view: ManualScrobbleView? = null
    private val compositeDisposable = CompositeDisposable()

    private var calendar = Calendar.getInstance()
    var year: Int = calendar.get(Calendar.YEAR)
    var month: Int = calendar.get(Calendar.MONTH)
    var day: Int = calendar.get(Calendar.DAY_OF_MONTH)
    var hour: Int = calendar.get(Calendar.HOUR_OF_DAY)
    var minute: Int = calendar.get(Calendar.MINUTE)

    fun attachView(view: ManualScrobbleView){
        this.view = view
    }

    fun detachView(){
        view = null
        compositeDisposable.clear()
    }

    private fun onException(exception: Throwable) {
        view?.hideProgressBar()
        view?.enableScrobbleButton()
        view?.showErrorMessage(ExceptionHandler().sendExceptionAndGetReadableMessage(exception))
    }

    fun onScrobbleButtonClick(track: String, artist: String, album: String, trackNumber: String, trackDuration: String){
        if (ConnectionChecker.isNetworkAvailable()) {
            if (TextUtils.isDigitsOnly(trackNumber) && TextUtils.isDigitsOnly(trackDuration)) {
                view?.disableScrobbleButton()
                view?.showProgressBar()

                compositeDisposable.add(
                        AppContext.getInstance().retrofitWebService.sendScrobble(AppContext.getInstance().sessionKey, track, artist, album, trackNumber, trackDuration, calculateUnixTime())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        {
                                            onSendScrobbleResult(it)
                                        },
                                        {
                                            onException(it)
                                        }
                                )
                )
            } else
                view?.showNonnumericalInputMessage()
        }
        else {
            view?.showNoConnectionMessage()
        }
    }

    fun onDateSet(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        this.year = year
        this.month = monthOfYear
        this.day = dayOfMonth

        if (view?.didAllRequiredFieldsSet() == true)
            view?.enableScrobbleButton()
    }

    fun onTimeSet(hourOfDay: Int, minute: Int) {
        this.hour = hourOfDay
        this.minute = minute

        if (view?.didAllRequiredFieldsSet() == true)
            view?.enableScrobbleButton()
    }

    private fun onSendScrobbleResult(result: SendScrobbleResult) {
        view?.hideProgressBar()

        when (result.code) {
            0 -> view?.showScrobbleAcceptedMessage()
            1 -> {
                view?.enableScrobbleButton()
                view?.showScrobbleIgnoredMessage()
                Crashlytics.logException(APIException(result.message))
            }
            2 -> {
                view?.enableScrobbleButton()
                view?.showTrackIgnoredMessage()
                Crashlytics.logException(APIException(result.message))
            }
            3 -> {
                view?.enableScrobbleButton()
                view?.showTimestampOldMessage()
                Crashlytics.logException(APIException(result.message))
            }
            4 -> {
                view?.enableScrobbleButton()
                view?.showTimestampNewMessage()
                Crashlytics.logException(APIException(result.message))
            }
            5 -> {
                view?.enableScrobbleButton()
                view?.showScrobblesLimitMessage()
                Crashlytics.logException(APIException(result.message))
            }
        }
    }

    private fun calculateUnixTime(): Long {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        return TimeUnit.MILLISECONDS.toSeconds(calendar.timeInMillis)
    }
}
