package by.d1makrat.library_fm.ui.fragment;


import android.app.Activity;

import java.util.Calendar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;

import by.d1makrat.library_fm.BuildConfig;
import by.d1makrat.library_fm.NetworkStatusChecker;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.SendScrobbleAsynctaskCallback;
import by.d1makrat.library_fm.asynctask.SendScrobbleTask;

public class ManualScrobbleFragment extends Fragment implements CalendarDatePickerDialogFragment.OnDateSetListener, RadialTimePickerDialogFragment.OnTimeSetListener, SendScrobbleAsynctaskCallback {

	private final String API_KEY = BuildConfig.API_KEY;
	private String sessionKey;
	private String username;
	private SendScrobbleTask task;
	private int mYear, mMonth, mDay, mHour, mMinute;
	private Calendar cal;
	private ProgressBar spinner;
	public TextView track, artist, album, trackduration, tracknumber;
	private SendScrobbleAsynctaskCallback mCallback;

	@Override
	public void onCreate(Bundle savedInstance){
		super.onCreate(savedInstance);

		mCallback = this;

		sessionKey = getArguments().getString("sessionKey");
		username = getArguments().getString("username");
		cal = Calendar.getInstance();
		mYear = cal.get(Calendar.YEAR);
		mMonth = cal.get(Calendar.MONTH);
		mDay = cal.get(Calendar.DAY_OF_MONTH);
		mHour = cal.get(Calendar.HOUR_OF_DAY);
		mMinute = cal.get(Calendar.MINUTE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View rootView = inflater.inflate(R.layout.activity_manualscrobble, container, false);
		((Button) rootView.findViewById(R.id.button_scrobble)).setEnabled(true);

		rootView.findViewById(R.id.button_scrobble).setOnClickListener(pressListener);
		rootView.findViewById(R.id.button_date).setOnClickListener(pressListener);
		rootView.findViewById(R.id.button_time).setOnClickListener(pressListener);
		rootView.setOnClickListener(pressListener);
		spinner = (ProgressBar) rootView.findViewById(R.id.progressBar);
		spinner.setVisibility(View.INVISIBLE);

		((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.manual_scrobble));


		artist = (TextView) rootView.findViewById(R.id.artist);
		track = (TextView) rootView.findViewById(R.id.track);
		album = (TextView) rootView.findViewById(R.id.album);
		trackduration = (TextView) rootView.findViewById(R.id.trackduration);
		tracknumber = (TextView) rootView.findViewById(R.id.tracknumber);

		return rootView;
	}

	public View.OnClickListener pressListener = new View.OnClickListener() {
		public void onClick(View v) {
			InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

//			Configuration config = new Configuration();
//			Toast.makeText(getContext(), config.getLocales().get(0).toString(), Toast.LENGTH_LONG).show();


			switch (v.getId()) {
				case R.id.button_scrobble:
					if (NetworkStatusChecker.isNetworkAvailable()) {
//					((Button) getView().findViewById(R.id.button_scrobble)).setEnabled(false);
					spinner.setVisibility(View.VISIBLE);
					String track = ((TextView) getView().findViewById(R.id.track)).getText().toString();
					String artist = ((TextView) getView().findViewById(R.id.artist)).getText().toString();
					String album = ((TextView) getView().findViewById(R.id.album)).getText().toString();
					String tracknumber = ((TextView) getView().findViewById(R.id.tracknumber)).getText().toString();
					String duration = ((TextView) getView().findViewById(R.id.trackduration)).getText().toString();

					cal = Calendar.getInstance();
					cal.set(mYear, mMonth, mDay, mHour, mMinute, 0);
//					Log.d("DEBUG", TimeZone.getDefault().toString());
//					cal.setTimeZone(TimeZone.getTimeZone(TIMEZONE));
					Long timestamp = (cal.getTimeInMillis() / 1000L);
						if (artist.equals("") || track.equals("") || duration.equals("")) {
							spinner.setVisibility(View.INVISIBLE);
							Toast toast = Toast.makeText(v.getContext(), "Fill fields \"Track title\", \"Artist\", \"Duration\"", Toast.LENGTH_SHORT);
							toast.show();
						} else if (TextUtils.isDigitsOnly(tracknumber) && TextUtils.isDigitsOnly(duration)){
						try {
							String[] asynctaskArgs = new String[]{track, artist, album, String.valueOf(tracknumber), String.valueOf(duration), String.valueOf(timestamp)};
							task = new SendScrobbleTask(mCallback);
							task.execute(asynctaskArgs);
						} catch (NumberFormatException exception){
							onException(exception);
						}
						}
                    }
                    else {
                        Toast toast;
                        toast = Toast.makeText(getContext(), R.string.network_is_not_connected, Toast.LENGTH_SHORT);
                        toast.show();
                    }
					break;
				case R.id.button_date:

							CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
									.setThemeCustom(R.style.MyCustomBetterPickersDialogs)
									.setOnDateSetListener(ManualScrobbleFragment.this)
									.setFirstDayOfWeek(Calendar.MONDAY)
									.setPreselectedDate(mYear, mMonth, mDay)
//									.setDateRange(minDate, null)
									.setDoneText("OK")
									.setCancelText("Cancel");
//									.setThemeDark();
							cdp.show(getActivity().getSupportFragmentManager(), "DatePicker");




//					DatePickerFragment date = new DatePickerFragment();
//					date.show(getActivity().getFragmentManager(), "DatePicker");
					break;
				case R.id.button_time:

					RadialTimePickerDialogFragment rtp = new RadialTimePickerDialogFragment()
							.setThemeCustom(R.style.MyCustomBetterPickersDialogs)
							.setOnTimeSetListener(ManualScrobbleFragment.this)
							.setStartTime(mHour, mMinute)
							.setDoneText("OK")
							.setCancelText("Cancel");
//							.setThemeDark();
					rtp.show(getActivity().getSupportFragmentManager(), "TimePicker");

//					TimePickerFragment time = new TimePickerFragment();
//					time.show(getActivity().getFragmentManager(), "TimePicker");
					break;
				default:
					break;
			}
		}

	};


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
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
	}

	@Override
	public void onStop(){
		super.onStop();
		KillTaskIfRunning(task);
	}

	private void KillTaskIfRunning(AsyncTask task) {
		if (task != null && task.getStatus() != AsyncTask.Status.FINISHED){
			task.cancel(true);
		}
	}

	@Override
	public void onException(Exception pException) {
		spinner.setVisibility(View.INVISIBLE);
		Toast.makeText(getContext(), pException.getMessage(), Toast.LENGTH_LONG).show();
	}

	@Override
	public void onSendScrobbleResult(String result) {
		spinner.setVisibility(View.INVISIBLE);
		Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
	}

//	public class DatePickerFragment extends DialogFragment implements OnDateSetListener {
//
//		@Override
//		public Dialog onCreateDialog(Bundle savedInstanceState) {
//			DatePickerDialog dp = new DatePickerDialog(getActivity(),  R.style.MyDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
//				@Override
//				public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//					mYear = year;
//					mMonth = monthOfYear;
//					mDay = dayOfMonth;
//				}}, mYear, mMonth, mDay);
//			dp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#9a0202")));
////			dp.setTitle("Set date");
//			dp.getDatePicker().setCalendarViewShown(false);
//			return dp;
//		}
//
//		@Override
//		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//		}
//	}
//
//	public class TimePickerFragment extends DialogFragment implements OnTimeSetListener {
//
//		@Override
//		public Dialog onCreateDialog(Bundle savedInstanceState) {
//			TimePickerDialog tp = new TimePickerDialog(getActivity(), R.style.MyTimePickerDialogTheme, new TimePickerDialog.OnTimeSetListener() {
//				@Override
//				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//					mHour = hourOfDay;
//					mMinute = minute;
//				}}, mHour, mMinute, true);
////			tp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#9a0202")));
////			tp.setTitle("Set time");
//			return tp;
//		}
//
//		@Override
//		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//		}
//	}

}