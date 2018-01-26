package by.d1makrat.library_fm.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.asynctask.SendScrobbleCallback;
import by.d1makrat.library_fm.asynctask.SendScrobbleAsyncTask;
import by.d1makrat.library_fm.https.HttpsClient;
import by.d1makrat.library_fm.utils.InputUtils;

public class ManualScrobbleFragment extends Fragment implements CalendarDatePickerDialogFragment.OnDateSetListener, RadialTimePickerDialogFragment.OnTimeSetListener, SendScrobbleCallback {

	private SendScrobbleAsyncTask mSendScrobbleAsyncTask;
	private int mYear, mMonth, mDay, mHour, mMinute;
	private Calendar mCalendar;
	private View mSpinner;
	private SendScrobbleCallback mCallback;

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

		final View rootView = inflater.inflate(R.layout.fragment_manualscrobble, container, false);
		(rootView.findViewById(R.id.button_scrobble)).setEnabled(true);

		rootView.findViewById(R.id.button_scrobble).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				InputUtils.hideKeyboard(getActivity());
				if (HttpsClient.isNetworkAvailable()) {
					String track = ((TextView) rootView.findViewById(R.id.track)).getText().toString();
					String artist = ((TextView) rootView.findViewById(R.id.artist)).getText().toString();
					String album = ((TextView) rootView.findViewById(R.id.album)).getText().toString();
					String trackNumber = ((TextView) rootView.findViewById(R.id.tracknumber)).getText().toString();
					String trackDuration = ((TextView) rootView.findViewById(R.id.trackduration)).getText().toString();

					mCalendar = Calendar.getInstance();
					mCalendar.set(mYear, mMonth, mDay, mHour, mMinute, 0);
					Long timestamp = (TimeUnit.MILLISECONDS.toSeconds(mCalendar.getTimeInMillis()));

					if (artist.equals("") || track.equals("") || trackDuration.equals("")) {
						mSpinner.setVisibility(View.INVISIBLE);
						Toast.makeText(getContext(), getString(R.string.manual_fragment_fill_required_fields), Toast.LENGTH_SHORT).show();
					}
					else
					if (TextUtils.isDigitsOnly(trackNumber) && TextUtils.isDigitsOnly(trackDuration)){
						v.setEnabled(false);
						mSpinner.setVisibility(View.VISIBLE);
						String[] asynctaskArgs = new String[]{track, artist, album, String.valueOf(trackNumber), String.valueOf(trackDuration), String.valueOf(timestamp)};
						mSendScrobbleAsyncTask = new SendScrobbleAsyncTask(mCallback);
						mSendScrobbleAsyncTask.execute(asynctaskArgs);
					}
					else
						Toast.makeText(getContext(), getString(R.string.manual_fragment_nonnumerical_input), Toast.LENGTH_SHORT).show();
				}
				else
					Toast.makeText(getContext(), R.string.network_is_not_connected, Toast.LENGTH_SHORT).show();
			}
		});
		rootView.findViewById(R.id.button_date).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				InputUtils.hideKeyboard(getActivity());
				CalendarDatePickerDialogFragment calendarDatePickerDialogFragment = new CalendarDatePickerDialogFragment()
						.setThemeCustom(R.style.CustomBetterPickersDialogs)
						.setOnDateSetListener(ManualScrobbleFragment.this)
						.setFirstDayOfWeek(Calendar.MONDAY)
						.setPreselectedDate(mYear, mMonth, mDay)
						.setDoneText("OK")
						.setCancelText("Cancel");
				calendarDatePickerDialogFragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
			}
		});
		rootView.findViewById(R.id.button_time).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				InputUtils.hideKeyboard(getActivity());
				RadialTimePickerDialogFragment radialTimePickerDialogFragment = new RadialTimePickerDialogFragment()
						.setThemeCustom(R.style.CustomBetterPickersDialogs)
						.setOnTimeSetListener(ManualScrobbleFragment.this)
						.setStartTime(mHour, mMinute)
						.setDoneText("OK")
						.setCancelText("Cancel");
				radialTimePickerDialogFragment.show(getActivity().getSupportFragmentManager(), "TimePicker");
			}
		});
		rootView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				InputUtils.hideKeyboard(getActivity());
			}
		});

		mSpinner = rootView.findViewById(R.id.progressBar);
		mSpinner.setVisibility(View.INVISIBLE);

		((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.manual_scrobble));

		return rootView;
	}

//	private void hideKeyboard() {
//		InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
//		inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
//	}

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
		killTaskIfRunning(mSendScrobbleAsyncTask);
	}

	private void killTaskIfRunning(AsyncTask task) {
		if (task != null && task.getStatus() != AsyncTask.Status.FINISHED){
			task.cancel(true);
		}
	}

	@Override
	public void onException(Exception pException) {
		if (getView() != null)
			getView().findViewById(R.id.button_scrobble).setEnabled(true);
		mSpinner.setVisibility(View.INVISIBLE);
		Toast.makeText(getActivity(), pException.getMessage(), Toast.LENGTH_LONG).show();
	}

	@Override
	public void onSendScrobbleResult(String result) {
		if (getView() != null)
			getView().findViewById(R.id.button_scrobble).setEnabled(true);
		mSpinner.setVisibility(View.INVISIBLE);
		Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
	}
}