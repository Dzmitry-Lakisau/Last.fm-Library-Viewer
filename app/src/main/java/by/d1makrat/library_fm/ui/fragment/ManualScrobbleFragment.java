package by.d1makrat.library_fm.ui.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;

import java.util.Calendar;

import by.d1makrat.library_fm.NetworkStatusChecker;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.SendScrobbleAsynctaskCallback;
import by.d1makrat.library_fm.asynctask.SendScrobbleTask;

public class ManualScrobbleFragment extends Fragment implements CalendarDatePickerDialogFragment.OnDateSetListener, RadialTimePickerDialogFragment.OnTimeSetListener, SendScrobbleAsynctaskCallback {

	private SendScrobbleTask mSendScrobbleTask;
	private int mYear, mMonth, mDay, mHour, mMinute;
	private Calendar mCalendar;
	private ProgressBar spinner;
	private SendScrobbleAsynctaskCallback mCallback;

	@Override
	public void onCreate(Bundle savedInstance){
		super.onCreate(savedInstance);

		mCallback = this;

		mCalendar = Calendar.getInstance();
		mYear = mCalendar.get(Calendar.YEAR);
		mMonth = mCalendar.get(Calendar.MONTH);
		mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
		mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
		mMinute = mCalendar.get(Calendar.MINUTE);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		View rootView = inflater.inflate(R.layout.activity_manualscrobble, container, false);
		(rootView.findViewById(R.id.button_scrobble)).setEnabled(true);

		rootView.findViewById(R.id.button_scrobble).setOnClickListener(pressListener);
		rootView.findViewById(R.id.button_date).setOnClickListener(pressListener);
		rootView.findViewById(R.id.button_time).setOnClickListener(pressListener);
		rootView.setOnClickListener(pressListener);

		spinner = rootView.findViewById(R.id.progressBar);
		spinner.setVisibility(View.INVISIBLE);

		((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.manual_scrobble));

		return rootView;
	}

	public View.OnClickListener pressListener = new View.OnClickListener() {
		public void onClick(View view) {
			hideKeyboard();

			switch (view.getId()) {
				case R.id.button_scrobble:
					if (NetworkStatusChecker.isNetworkAvailable()) {
						String track = ((TextView) getView().findViewById(R.id.track)).getText().toString();
						String artist = ((TextView) getView().findViewById(R.id.artist)).getText().toString();
						String album = ((TextView) getView().findViewById(R.id.album)).getText().toString();
						String trackNumber = ((TextView) getView().findViewById(R.id.tracknumber)).getText().toString();
						String trackDuration = ((TextView) getView().findViewById(R.id.trackduration)).getText().toString();

						mCalendar = Calendar.getInstance();
						mCalendar.set(mYear, mMonth, mDay, mHour, mMinute, 0);
						Long timestamp = (mCalendar.getTimeInMillis() / 1000L);

						if (artist.equals("") || track.equals("") || trackDuration.equals("")) {
							spinner.setVisibility(View.INVISIBLE);
							Toast.makeText(view.getContext(), "Fill required fields \"Track title\", \"Artist\", \"Duration\"", Toast.LENGTH_SHORT).show();
						}
						else
						if (TextUtils.isDigitsOnly(trackNumber) && TextUtils.isDigitsOnly(trackDuration)){
							view.setEnabled(false);
							spinner.setVisibility(View.VISIBLE);
							String[] asynctaskArgs = new String[]{track, artist, album, String.valueOf(trackNumber), String.valueOf(trackDuration), String.valueOf(timestamp)};
							mSendScrobbleTask = new SendScrobbleTask(mCallback);
							mSendScrobbleTask.execute(asynctaskArgs);
						}
						else
							Toast.makeText(view.getContext(), "Nonnumerical input", Toast.LENGTH_SHORT).show();
					}
					else
						Toast.makeText(getContext(), R.string.network_is_not_connected, Toast.LENGTH_SHORT).show();
					break;
				case R.id.button_date:
					hideKeyboard();
					CalendarDatePickerDialogFragment calendarDatePickerDialogFragment = new CalendarDatePickerDialogFragment()
							.setThemeCustom(R.style.CustomBetterPickersDialogs)
							.setOnDateSetListener(ManualScrobbleFragment.this)
							.setFirstDayOfWeek(Calendar.MONDAY)
							.setPreselectedDate(mYear, mMonth, mDay)
							.setDoneText("OK")
							.setCancelText("Cancel");
					calendarDatePickerDialogFragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
					break;
				case R.id.button_time:
					hideKeyboard();
					RadialTimePickerDialogFragment radialTimePickerDialogFragment = new RadialTimePickerDialogFragment()
							.setThemeCustom(R.style.CustomBetterPickersDialogs)
							.setOnTimeSetListener(ManualScrobbleFragment.this)
							.setStartTime(mHour, mMinute)
							.setDoneText("OK")
							.setCancelText("Cancel");
					radialTimePickerDialogFragment.show(getActivity().getSupportFragmentManager(), "TimePicker");
					break;
				default:
					break;
			}
		}
	};

	private void hideKeyboard() {
		InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
	}

	@Override
	public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
		mYear = year;
		mMonth = monthOfYear;
		mDay = dayOfMonth;
	}

	@Override
	public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
		mHour = hourOfDay;
		mMinute = minute;
	}

	@Override
	public void onStop(){
		super.onStop();
		KillTaskIfRunning(mSendScrobbleTask);
	}

	private void KillTaskIfRunning(AsyncTask task) {
		if (task != null && task.getStatus() != AsyncTask.Status.FINISHED){
			task.cancel(true);
		}
	}

	@Override
	public void onException(Exception pException) {
		if (getView() != null)
			getView().findViewById(R.id.button_scrobble).setEnabled(false);
		spinner.setVisibility(View.INVISIBLE);
		Toast.makeText(getContext(), pException.getMessage(), Toast.LENGTH_LONG).show();
	}

	@Override
	public void onSendScrobbleResult(String result) {
		if (getView() != null)
			getView().findViewById(R.id.button_scrobble).setEnabled(false);
		spinner.setVisibility(View.INVISIBLE);
		Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
	}
}