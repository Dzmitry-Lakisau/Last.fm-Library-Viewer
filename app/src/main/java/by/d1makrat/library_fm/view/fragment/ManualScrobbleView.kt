package by.d1makrat.library_fm.view.fragment

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment

interface ManualScrobbleView: CalendarDatePickerDialogFragment.OnDateSetListener, RadialTimePickerDialogFragment.OnTimeSetListener {
    fun didAllRequiredFieldsSet(): Boolean
    fun disableScrobbleButton()
    fun enableScrobbleButton()
    fun hideProgressBar()
    fun showNonnumericalInputMessage()
    fun showNoConnectionMessage()
    fun showProgressBar()
    fun showErrorMessage(message: String)
    fun showScrobbleAcceptedMessage()
    fun showScrobbleIgnoredMessage()
    fun showScrobblesLimitMessage()
    fun showTimestampOldMessage()
    fun showTimestampNewMessage()
    fun showTrackIgnoredMessage()
}
