package by.d1makrat.library_fm.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import by.d1makrat.library_fm.R
import by.d1makrat.library_fm.R.string.*
import by.d1makrat.library_fm.presenter.fragment.ManualScrobblePresenter
import by.d1makrat.library_fm.ui.CenteredToast
import by.d1makrat.library_fm.utils.InputUtils.hideKeyboard
import by.d1makrat.library_fm.view.fragment.ManualScrobbleView
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment
import kotlinx.android.synthetic.main.fragment_manualscrobble.*
import kotlinx.android.synthetic.main.fragment_manualscrobble.view.*
import java.util.*

class ManualScrobbleFragment: Fragment(), ManualScrobbleView {

    val PICKER_OK = "OK"
    val PICKER_CANCEL = "Cancel"
    val DATE_PICKER_TAG = "DatePickerFragment"
    val TIME_PICKER_TAG = "TimePickerFragment"

    val presenter = ManualScrobblePresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_manualscrobble, container, false)

        rootView.setOnClickListener { hideKeyboard((activity)) }

        rootView.button_scrobble.setOnClickListener {
            hideKeyboard(activity)

            val track = edit_track.text.toString()
            val artist = edit_artist.text.toString()
            val album = edit_album.text.toString()
            val trackNumber = edit_trackNumber.text.toString()
            val trackDuration = edit_trackDuration.text.toString()
            presenter.onScrobbleButtonClick(track, artist, album, trackNumber, trackDuration)
        }

        rootView.edit_track.addTextChangedListener(textWatcher)
        rootView.edit_artist.addTextChangedListener(textWatcher)
        rootView.edit_trackDuration.addTextChangedListener(textWatcher)

        rootView.button_date.setOnClickListener {
            hideKeyboard(activity)

            val calendarDatePickerDialogFragment = CalendarDatePickerDialogFragment()
                    .setThemeCustom(R.style.CustomBetterPickersDialogs)
                    .setOnDateSetListener(this)
                    .setFirstDayOfWeek(Calendar.MONDAY)
                    .setPreselectedDate(presenter.year, presenter.month, presenter.day)
                    .setDoneText(PICKER_OK)
                    .setCancelText(PICKER_CANCEL)
            calendarDatePickerDialogFragment.show(activity?.supportFragmentManager, DATE_PICKER_TAG)
        }

        rootView.button_time.setOnClickListener {
            hideKeyboard(activity)

            val radialTimePickerDialogFragment = RadialTimePickerDialogFragment()
                    .setThemeCustom(R.style.CustomBetterPickersDialogs)
                    .setOnTimeSetListener(this)
                    .setStartTime(presenter.hour, presenter.minute)
                    .setDoneText(PICKER_OK)
                    .setCancelText(PICKER_CANCEL)
            radialTimePickerDialogFragment.show(activity?.supportFragmentManager, TIME_PICKER_TAG)
        }

        presenter.attachView(this)

        return rootView
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            button_scrobble.isEnabled = didAllRequiredFieldsSet()
        }
    }

    override fun onDestroyView() {
        presenter.detachView()

        super.onDestroyView()
    }

    override fun onDateSet(dialog: CalendarDatePickerDialogFragment?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        presenter.onDateSet(year, monthOfYear, dayOfMonth)
    }

    override fun onTimeSet(dialog: RadialTimePickerDialogFragment?, hourOfDay: Int, minute: Int) {
        presenter.onTimeSet(hourOfDay, minute)
    }

    override fun disableScrobbleButton() {
        button_scrobble.isEnabled = false
    }

    override fun enableScrobbleButton() {
        button_scrobble.isEnabled = true
    }

    override fun hideProgressBar() {
        progressBar_scrobble.visibility = View.INVISIBLE
    }

    override fun showNonnumericalInputMessage() {
        CenteredToast.show(activity, R.string.manual_fragment_nonnumerical_input, Toast.LENGTH_SHORT)
    }

    override fun showNoConnectionMessage() {
        CenteredToast.show(activity, R.string.network_is_not_connected, Toast.LENGTH_SHORT)
    }

    override fun showProgressBar() {
        progressBar_scrobble.visibility = View.VISIBLE
    }

    override fun showErrorMessage(message: String) {
        CenteredToast.show(activity, message, Toast.LENGTH_SHORT)
    }

    override fun didAllRequiredFieldsSet(): Boolean {
        return edit_artist.text.toString().isNotEmpty() &&
                edit_track.text.toString().isNotEmpty() &&
                edit_trackDuration.text.toString().isNotEmpty()
    }

    override fun showScrobbleAcceptedMessage() {
        CenteredToast.show(activity, getString(manual_fragment_scrobble_accepted), Toast.LENGTH_SHORT)
    }

    override fun showScrobbleIgnoredMessage() {
        CenteredToast.show(activity, getString(manual_fragment_ignored_message), Toast.LENGTH_LONG)
    }

    override fun showScrobblesLimitMessage() {
        CenteredToast.show(activity, getString(manual_fragment_limit_exceeded), Toast.LENGTH_LONG)
    }

    override fun showTimestampOldMessage() {
        CenteredToast.show(activity, getString(manual_fragment_timestamp_old), Toast.LENGTH_LONG)
    }

    override fun showTimestampNewMessage() {
        CenteredToast.show(activity, getString(manual_fragment_timestamp_new), Toast.LENGTH_LONG)
    }

    override fun showTrackIgnoredMessage() {
        CenteredToast.show(activity, getString(manual_fragment_track_ignored), Toast.LENGTH_LONG)
    }
}
