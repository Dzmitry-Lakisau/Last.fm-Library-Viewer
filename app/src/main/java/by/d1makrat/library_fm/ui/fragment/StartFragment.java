package by.d1makrat.library_fm.ui.fragment;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.xmlpull.v1.XmlPullParserException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import javax.net.ssl.SSLException;

import by.d1makrat.library_fm.APIException;
import by.d1makrat.library_fm.BuildConfig;
import by.d1makrat.library_fm.Data;
import by.d1makrat.library_fm.NetworkStatusChecker;
import by.d1makrat.library_fm.R;

public class StartFragment extends Fragment {
    private final String API_KEY = BuildConfig.API_KEY;
    private String username = null;

    private GetPlaycountTask task;

    private String sessionKey = null;

    private String cachepath = null;
    String resolution;
    String url = null;
//    ViewGroup container = null;
    String registered = null;
    String playcount = null;
    private Menu menu;
    private static final int ABOUT_DIALOG = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        sessionKey = getArguments().getString("sessionKey");
        username = getArguments().getString("username");
//        limit = getArguments().getString("limit", "10");
        cachepath = getArguments().getString("cachepath");
//        resolution = getArguments().getString("resolution");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        registered = sharedPreferences.getString("registered", null);
        playcount = sharedPreferences.getString("playcount", null);
        url = "https://www.last.fm/user/" + username + "/library";

        setHasOptionsMenu(true);

//        try {
//            Class.forName("GetScrobblesListTask");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//            Toast.makeText(getContext(), "GetScrobblesListTask Class", Toast.LENGTH_SHORT).show();
//        }
//        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.start_layout, container, false);

        AdView adView = (AdView) rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(null);

//        String playcount = result;
        File avatar = new File(cachepath + File.separator + "UserAvatars" + File.separator + username + ".png");
        if (avatar.exists()) {
            ImageView imageView1 = (ImageView) rootView.findViewById(R.id.user_avatar_start_screen);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bm = BitmapFactory.decodeFile(avatar.getPath(), options);
            imageView1.setImageBitmap(bm);
        }
        TextView textView = (TextView) rootView.findViewById(R.id.hello_start_screen);
        textView.setText(String.format("You are logged in as %s.\n%s scrobbles since %s.\nExplore your music!", username, playcount, registered));
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        this.menu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                AboutDialogFragment dialogFragment = new AboutDialogFragment();
//                LayoutInflater layoutInflater = dialogFragment.getLayoutInflater(null);
//                layoutInflater.inflate(R.layout.dialog, null);
                dialogFragment.setTargetFragment(this, 0);
                dialogFragment.show(getFragmentManager(), "AboutDialogFragment");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @Override
//    protected Dialog onCreateDialog(int id) {
//
//        Dialog dialog = null;
//        AlertDialog.Builder dialogbuilder = null;
//
//        switch (id) {
//            case ABOUT_DIALOG:
//                dialogbuilder = new AlertDialog.Builder(this);
//                linearlayout = getLayoutInflater().inflate(R.layout.about_layout, null);
//                dialogbuilder.setView(linearlayout);
//                dialog = dialogbuilder.create();
//                dialog.show();//TODO WTF
//                break;
//        }
//        return dialog;
//    }

    @Override
    public void onStart() {
        super.onStart();
        if (NetworkStatusChecker.isNetworkAvailable()) {
            task = new GetPlaycountTask();
            task.execute();
        }
        else {
            Toast toast;
            toast = Toast.makeText(getContext(), R.string.network_is_not_connected, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

//    private void KillTaskIfRunning(AsyncTask task) {
//        if ((task.getStatus() != AsyncTask.Status.FINISHED)) {
//            page--;
//            task.cancel(true);
//        }
//    }

    class GetPlaycountTask extends AsyncTask<Void, Void, String> {
        private int exception = 0;
        private String message = null;

        @Override
        protected String doInBackground(Void... params) {
            String info = null;

            try {
                URL url = new URL("https://ws.audioscrobbler.com/2.0/?api_key=" + API_KEY + "&method=user.getInfo&sk=" + sessionKey);
                Data rawxml = new Data(url);
                if (rawxml.parseAttribute("lfm", "status").equals("failed")) {
                    throw new APIException(rawxml.parseSingleText("error"));
                }
                else info = rawxml.parseSingleText("playcount");
            }
            catch (XmlPullParserException e){
                //FirebaseCrash.report(e);
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
                //FirebaseCrash.report(e);
                e.printStackTrace();
                exception = 6;
            }
            catch (SSLException e) {
                //FirebaseCrash.report(e);
                e.printStackTrace();
                exception = 5;
            }
            catch (FileNotFoundException e){
                //FirebaseCrash.report(e);
                e.printStackTrace();
                exception = 4;
            }
            catch (RuntimeException e){
                //FirebaseCrash.report(e);
                e.printStackTrace();
                exception = 3;
            }
            catch (IOException e){
                //FirebaseCrash.report(e);
                e.printStackTrace();
                exception = 2;
            }
            catch (APIException e){
                //FirebaseCrash.report(e);
                message = e.getMessage();
                exception = 1;
            }
            return info;
        }

        @Override
        protected void onPostExecute(String result){
            if (exception == 0){
                try {
                    TextView textView = (TextView) getView().findViewById(R.id.hello_start_screen);
                    textView.setText(String.format("You are logged in as %s.\n%s scrobbles since %s.\nExplore your music!", username, result, registered));
                }
                catch (Exception e){
                    //FirebaseCrash.report(e);
                    e.printStackTrace();
//                    Toast.makeText(getContext(), "Unknown error occurred", Toast.LENGTH_SHORT).show();
                }
            }
            else if (exception == 1)
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            else {
                String[] exception_message = getResources().getStringArray(R.array.Exception_messages);
                Toast.makeText(getContext(), exception_message[exception - 1], Toast.LENGTH_SHORT).show();
            }
        }
    }
}
