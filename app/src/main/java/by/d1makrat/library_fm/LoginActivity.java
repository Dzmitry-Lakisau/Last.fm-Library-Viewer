package by.d1makrat.library_fm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlPullParserException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import javax.net.ssl.SSLException;
import static android.os.Environment.*;

public class LoginActivity extends AppCompatActivity {

    private final String API_KEY = BuildConfig.API_KEY;
    private final String SECRET = BuildConfig.SECRET;

    GetSessionKeyTask task_get_key;
    GetUserInfoTask task_info;
    private TextView username_field = null;
    private TextView password_field = null;
    private String cachepath = null;
    private String sessionKey = null;

    private ProgressBar spinner = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        Locale locale = new Locale("en_GB");
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.locale = locale;
//        getApplicationContext().getResources().updateConfiguration(config, null);


        super.onCreate(savedInstanceState);
        cachepath = getDiskCacheDir(getApplicationContext());
        setContentView(R.layout.sign_in_page);
        spinner = (ProgressBar) findViewById(R.id.progressBar2);
        username_field = (TextView) findViewById(R.id.login);
        password_field = (TextView) findViewById(R.id.password);

        CreateBlankAlbumart();
        CreatePersonImage();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);

        if (!sharedPreferences.contains("limit")){
            SharedPreferences.Editor spEditor = sharedPreferences.edit();
            spEditor.putString("limit", "10");
            spEditor.apply();
        }

        if (!sharedPreferences.contains("resolution")){
            SharedPreferences.Editor spEditor = sharedPreferences.edit();
            spEditor.putString("resolution", "medium");
            spEditor.apply();
        }

        if (sharedPreferences.getString("sessionKey", null) != null &&  sharedPreferences.getString("username", null) != null){
            startActivity(new Intent(this, MainActivity.class));
        }
        else {
            findViewById(R.id.sign_in_page).setOnClickListener(pressListener);
            findViewById(R.id.enter_button).setOnClickListener(pressListener);
        }

    }

    public View.OnClickListener pressListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            switch (v.getId()) {
                case R.id.enter_button:
                    if (isNetworkAvailable()) {
                        task_get_key = new GetSessionKeyTask();
                        task_get_key.execute(new String[]{username_field.getText().toString(), password_field.getText().toString()});
                    }
                    else {Toast toast;
                        toast = Toast.makeText(getApplicationContext(), R.string.network_is_not_connected, Toast.LENGTH_SHORT);
                        toast.show();}
                    break;
                default:
//                    ImageView imageView = (ImageView) findViewById(R.id.imageView);
//                    Log.d("DEBUG", "height: " + String.valueOf(imageView.getHeight()) + " width: " + String.valueOf(imageView.getWidth()));
                    break;
            }
        }
    };

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void CreateBlankAlbumart(){
        boolean isSucceeded = false;
        while(!isSucceeded) {
            try {
                String filename = getFilesDir().getPath() + File.separator + "blank_albumart.png";
                if (!(new File(filename).exists())) {
                    Drawable d = getResources().getDrawable(R.drawable.default_albumart);
                    Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
                    FileOutputStream out = new FileOutputStream(filename);
                    isSucceeded = bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                }
                else isSucceeded = true;
            }
            catch (Exception e){
                FirebaseCrash.report(e);
                e.printStackTrace();
                Toast.makeText(this, "Unable to create required bitmap file", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void CreatePersonImage(){
        boolean isSucceeded = false;
        while(!isSucceeded) {
            try {
                String filename = getFilesDir().getPath() + File.separator + "ic_person.png";
                if (!(new File(filename).exists())) {
                    Drawable d = getResources().getDrawable(R.drawable.ic_person);
                    Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
                    FileOutputStream out = new FileOutputStream(filename);
                    isSucceeded = bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                }
                else isSucceeded = true;
            }
            catch (Exception e){
                FirebaseCrash.report(e);
                e.printStackTrace();
                Toast.makeText(this, "Unable to create required bitmap file", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getDiskCacheDir(Context context) {
        try {
            final String cachePath =
                    Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !isExternalStorageRemovable() ? context.getExternalCacheDir().getPath() : context.getCacheDir().getPath();
            return new File(cachePath + File.separator + "Images").getPath();
        }
        catch (Exception e){
            FirebaseCrash.report(e);
            e.printStackTrace();
            Toast.makeText(context, "Unable to get path of cache folder", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    class GetSessionKeyTask extends AsyncTask<String[], Void, String> {
        private String message = null;
        private int exception = 0;

        @Override
        protected String doInBackground(String[]... params) {
            URL url = null;
            String res = null;
            try {
                String api_sig = new String(Hex.encodeHex(DigestUtils.md5("api_key" + API_KEY + "methodauth.getMobileSessionpassword" + params[0][1] + "username" + params[0][0] + SECRET)));
                url = new URL("https://ws.audioscrobbler.com/2.0/?method=auth.getMobileSession&api_key=" + API_KEY + "&username=" + params[0][0] + "&password=" + params[0][1] + "&api_sig=" + api_sig);
                Data rawxml = new Data(url);
                if (rawxml.parseAttribute("lfm", "status").equals("failed")) {
                    throw new APIException(rawxml.parseSingleText("error"));
                }
                else res = rawxml.parseSingleText("key");
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
        protected void onPostExecute(String result){
            if (exception == 0 && result.length() == 32){
                sessionKey = result;
                task_info = new GetUserInfoTask();
                task_info.execute(cachepath);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                SharedPreferences.Editor spEditor = sharedPreferences.edit();
                spEditor.putString("sessionKey", sessionKey);
                spEditor.apply();
            }
            else {
                username_field.setVisibility(View.VISIBLE);
                password_field.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.INVISIBLE);
                ((Button) findViewById(R.id.enter_button)).setVisibility(View.VISIBLE);
                if (exception == 1)
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                else {
                    String[] exception_message = getResources().getStringArray(R.array.Exception_names);
                    Toast.makeText(getApplicationContext(), exception_message[exception - 1], Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        protected void onPreExecute(){
            username_field.setVisibility(View.INVISIBLE);
            password_field.setVisibility(View.INVISIBLE);
            spinner.setVisibility(View.VISIBLE);
            ((Button) findViewById(R.id.enter_button)).setVisibility(View.INVISIBLE);
        }
    }


    class GetUserInfoTask extends AsyncTask<String, Void, HashMap<String, String>> {
        private int exception = 0;
        private String message = null;

        @Override
        protected HashMap<String, String> doInBackground(String... params) {
            HashMap<String, String> info = null;

            try {
                URL url = new URL("https://ws.audioscrobbler.com/2.0/?api_key=" + API_KEY + "&method=user.getInfo&sk=" + sessionKey);
                Data rawxml = new Data(url);
                if (rawxml.parseAttribute("lfm", "status").equals("failed")) {
                    throw new APIException(rawxml.parseSingleText("error"));
                }
                else info = rawxml.getUserInfo(getApplicationContext(), params[0]);
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
            catch (RuntimeException e){
                FirebaseCrash.report(e);
                e.printStackTrace();
                exception = 3;
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
            catch (SSLException e){
                FirebaseCrash.report(e);
                e.printStackTrace();
                exception = 5;
            }
            catch (FileNotFoundException e){
                FirebaseCrash.report(e);
                e.printStackTrace();
                exception = 4;
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
            return info;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> result){
            if (exception == 0){
                try {
                    Calendar mydate = Calendar.getInstance();
                    mydate.setTimeInMillis(Long.parseLong(result.get("registered")) * 1000);
                    String registered = mydate.get(Calendar.DAY_OF_MONTH) + " " + mydate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH) + " " + mydate.get(Calendar.YEAR);
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    SharedPreferences.Editor spEditor = sharedPreferences.edit();
                    spEditor.putString("username", result.get("name"));
                    spEditor.putString("playcount", result.get("playcount").substring(0, result.get("playcount").indexOf(" ")));
                    spEditor.putString("registered", registered);
                    spEditor.apply();
                    spinner.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
                catch (Exception e){
                    FirebaseCrash.report(e);
                    e.printStackTrace();
                }
            }
            else if (exception == 1)
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            else {
                String[] exception_message = getResources().getStringArray(R.array.Exception_names);
                Toast.makeText(getApplicationContext(), exception_message[exception - 1], Toast.LENGTH_SHORT).show();
            }
        }
        //TODO Firebase Test Lab
    }
}

