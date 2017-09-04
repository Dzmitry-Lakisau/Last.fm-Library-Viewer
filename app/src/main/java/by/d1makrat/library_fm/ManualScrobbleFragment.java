package by.d1makrat.library_fm;


import android.app.Activity;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.TreeMap;
import java.util.Calendar;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.crash.FirebaseCrash;

import org.xmlpull.v1.XmlPullParserException;
import javax.net.ssl.SSLException;

public class ManualScrobbleFragment extends Fragment implements CalendarDatePickerDialogFragment.OnDateSetListener, RadialTimePickerDialogFragment.OnTimeSetListener{

	private final String API_KEY = BuildConfig.API_KEY;
	private String sessionKey;
	private String username;
	private ScrobbleTask task;
	private int mYear, mMonth, mDay, mHour, mMinute;
	private Calendar cal;
	private ProgressBar spinner;
	public TextView track, artist, album, trackduration, tracknumber;

	@Override
	public void onCreate(Bundle savedInstance){
		super.onCreate(savedInstance);
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
					if (isNetworkAvailable()) {
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
						} else {
							TreeMap<String, String> treeMap = new TreeMap<>();
							treeMap.put("track", track);
							treeMap.put("artist", artist);
							treeMap.put("album", album);
							treeMap.put("trackNumber", tracknumber);
							treeMap.put("duration", duration);
							treeMap.put("timestamp", timestamp.toString());
							treeMap.put("method", "track.scrobble");
							treeMap.put("api_key", API_KEY);
							treeMap.put("sk", sessionKey);
							treeMap.put("user", username);
							task = new ScrobbleTask();
							task.execute(treeMap);
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

	public boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isConnected();
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

	class ScrobbleTask extends AsyncTask<TreeMap<String, String>, Void, String> {

		private int exception = 0;
		private String message = null;

		@Override
		protected String doInBackground(TreeMap<String, String>... params) {

			String res = null;

			try {
				Data rawxml = new Data(params[0]);
				if (rawxml.parseAttribute("lfm", "status").equals("failed")) {
					throw new APIException(rawxml.parseSingleText("error"));
				}
				else if (rawxml.parseAttribute("scrobbles", "accepted").equals("0"))
						throw new APIException(rawxml.parseAttribute("ignoredMessage", "code"));
					else res = "Accepted";
			}
			catch (XmlPullParserException e){
				FirebaseCrash.report(e);
				e.printStackTrace();
				exception = 9;
			}
			catch (UnknownHostException e) {
				e.printStackTrace();
				exception = 8;
			}
			catch (SocketTimeoutException e){
				e.printStackTrace();
				exception = 7;
			}
			catch (MalformedURLException e){
				FirebaseCrash.report(e);
				e.printStackTrace();
				exception = 6;
			}
			catch (SSLException e) {
				FirebaseCrash.report(e);
				e.printStackTrace();
				exception = 5;
			}
			catch (FileNotFoundException e){
				FirebaseCrash.report(e);
				e.printStackTrace();
				exception = 4;
			}
			catch (RuntimeException e){
				FirebaseCrash.report(e);
				e.printStackTrace();
				exception = 3;
			}
			catch (IOException e){
				FirebaseCrash.report(e);
				e.printStackTrace();
				exception = 2;
			}
			catch (APIException e){
				FirebaseCrash.report(e);
				message = e.getMessage();
				exception = 1;
			}
			return res;
		}

		@Override
		protected void onPostExecute(String result) {
			spinner.setVisibility(View.INVISIBLE);
			try {
//				((Button) getView().findViewById(R.id.button_scrobble)).setEnabled(true);
			}
			catch (Exception e){
				FirebaseCrash.report(e);
			}
			if (exception == 0){
				Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
			}
			else if (exception == 1)
				Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
			else {
				String[] exception_message = getResources().getStringArray(R.array.Exception_names);
				Toast.makeText(getContext(), exception_message[exception - 1], Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onCancelled() {
		}
	}
}