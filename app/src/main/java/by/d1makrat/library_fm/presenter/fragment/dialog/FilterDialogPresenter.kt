package by.d1makrat.library_fm.presenter.fragment.dialog

import by.d1makrat.library_fm.model.FilterRange
import by.d1makrat.library_fm.view.fragment.FilterDialogFragmentView
import java.util.*
import java.util.concurrent.TimeUnit

class FilterDialogPresenter(private var filterRange: FilterRange) {

    private val DATE_LAST_FM_LAUNCHED = 1016582400L

    private var view: FilterDialogFragmentView? = null

    var yearOfStart: Int? = null
    var monthOfStart: Int? = null
    var dayOfMonthOfStart: Int? = null
    var yearOfEnd: Int? = null
    var monthOfEnd: Int? = null
    var dayOfMonthOfEnd: Int? = null

    private var startOfPeriodCalendar = Calendar.getInstance()
    private var endOfPeriodCalendar = Calendar.getInstance()


    init {
        if (filterRange.startOfPeriod != null) {
            startOfPeriodCalendar.timeInMillis = TimeUnit.SECONDS.toMillis(filterRange.startOfPeriod!!)
        }
        if (filterRange.endOfPeriod != null) {
            endOfPeriodCalendar.timeInMillis = TimeUnit.SECONDS.toMillis(filterRange.endOfPeriod!!)
        }

        yearOfStart = startOfPeriodCalendar.get(Calendar.YEAR)
        monthOfStart = startOfPeriodCalendar.get(Calendar.MONTH)
        dayOfMonthOfStart = startOfPeriodCalendar.get(Calendar.DAY_OF_MONTH)
        yearOfEnd = endOfPeriodCalendar.get(Calendar.YEAR)
        monthOfEnd = endOfPeriodCalendar.get(Calendar.MONTH)
        dayOfMonthOfEnd = endOfPeriodCalendar.get(Calendar.DAY_OF_MONTH)
    }

    fun attachView(view: FilterDialogFragmentView){
        this.view = view
    }

    fun detachView(){
        view = null
    }

    fun onPositiveButtonClicked(yearOfStart: Int, monthOfStart: Int, dayOfMonthOfStart: Int, yearOfEnd: Int, monthOfEnd: Int, dayOfMonthOfEnd: Int) {
        startOfPeriodCalendar.set(yearOfStart, monthOfStart, dayOfMonthOfStart, 0, 0, 0)
        filterRange.startOfPeriod = TimeUnit.MILLISECONDS.toSeconds(startOfPeriodCalendar.timeInMillis)

        endOfPeriodCalendar.set(yearOfEnd, monthOfEnd, dayOfMonthOfEnd, 23, 59, 59)
        filterRange.endOfPeriod = TimeUnit.MILLISECONDS.toSeconds(endOfPeriodCalendar.timeInMillis)

        if (filterRange.startOfPeriod!! > filterRange.endOfPeriod!! || filterRange.endOfPeriod!! < DATE_LAST_FM_LAUNCHED) {
            view?.showWrongInputMessage()
        } else
            view?.returnToTargetFragment(filterRange)
    }

    fun onNeutralButtonClicked() {
        view?.returnToTargetFragment(FilterRange(null , null))
    }
}
