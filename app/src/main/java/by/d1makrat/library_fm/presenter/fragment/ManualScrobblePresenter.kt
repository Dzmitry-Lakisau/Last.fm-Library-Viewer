package by.d1makrat.library_fm.presenter.fragment

import android.text.TextUtils
import by.d1makrat.library_fm.asynctask.SendScrobbleAsyncTask
import by.d1makrat.library_fm.asynctask.SendScrobbleCallback
import by.d1makrat.library_fm.https.HttpsClient
import by.d1makrat.library_fm.view.fragment.ManualScrobbleView
import java.util.*
import java.util.concurrent.TimeUnit

class ManualScrobblePresenter: SendScrobbleCallback {

    private var view: ManualScrobbleView? = null

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
    }

    override fun onException(exception: Exception) {
        view?.hideProgressBar()
        view?.enableScrobbleButton()
        view?.showResult(exception.message!!)
    }

    fun onScrobbleButtonClick(track: String, artist: String, album: String, trackNumber: String, trackDuration: String){
        if (ConnectionChecker.isNetworkAvailable()) {
            if (TextUtils.isDigitsOnly(trackNumber) && TextUtils.isDigitsOnly(trackDuration)) {
                view?.disableScrobbleButton()
                view?.showProgressBar()
                val sendScrobbleAsyncTask = SendScrobbleAsyncTask(this)
                sendScrobbleAsyncTask.execute(track, artist, album, trackNumber, trackDuration, calculateUnixTime())
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
    }

    fun onTimeSet(hourOfDay: Int, minute: Int) {
        this.hour = hourOfDay
        this.minute = minute
    }

    override fun onSendScrobbleResult(result: String) {
        view?.hideProgressBar()
        view?.showResult(result)
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
