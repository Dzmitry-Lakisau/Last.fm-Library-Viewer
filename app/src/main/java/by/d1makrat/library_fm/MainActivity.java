package by.d1makrat.library_fm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.crash.FirebaseCrash;
import org.xmlpull.v1.XmlPullParserException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import javax.net.ssl.SSLException;
import static android.os.Environment.*;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final String API_KEY = BuildConfig.API_KEY;
    private String username = null;
    private String cachepath = null;
    private String registered = null;
    private String playcount = null;
    private String resolution = null;// = "medium";
    private String sessionKey = null;
    private FragmentManager fragmentManager;
    private boolean isLogout = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-2835136610089326~9892994294");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        username = sharedPreferences.getString("username", null);
        sessionKey = sharedPreferences.getString("sessionKey", null);
        registered = sharedPreferences.getString("registered", null);
        playcount = sharedPreferences.getString("playcount", null);
        cachepath = getDiskCacheDir(getApplicationContext());
        CreateNavigationDrawer();
        Bundle bundle = new Bundle();
        bundle.putString("sessionKey", sessionKey);
        bundle.putString("username", username);
        bundle.putString("cachepath", cachepath);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        StartFragment fragment = new StartFragment();
        if (fragment.getArguments() == null) fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.content_main, fragment, "StartFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (isNetworkAvailable()) {
            GetPlaycountTask task = new GetPlaycountTask();
            task.execute();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isLogout) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            SharedPreferences.Editor spEditor = sharedPreferences.edit();
            spEditor.putString("playcount", playcount);
            spEditor.apply();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        resolution = sharedPreferences.getString("resolution", "medium");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            if (isNetworkAvailable()) {
                GetPlaycountTask task = new GetPlaycountTask();
                task.execute();
            }
            switch (fragmentManager.getBackStackEntryCount()) {
                case 1:
                    finish();
                    break;
                default:
                    getSupportActionBar().setSubtitle(null);
                    super.onBackPressed();
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        getSupportActionBar().setSubtitle(null);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        int id = item.getItemId();
        Bundle bundle = new Bundle();
        bundle.putString("sessionKey", sessionKey);
        bundle.putString("username", username);
        bundle.putString("cachepath", cachepath);
        bundle.putString("resolution", resolution);

        if (isNetworkAvailable()) {
            GetPlaycountTask task = new GetPlaycountTask();
            task.execute();
        }
        else
        {
            Toast toast;
            toast = Toast.makeText(getApplicationContext(), R.string.network_is_not_connected, Toast.LENGTH_SHORT);
            toast.show();
        }
            Fragment fragment = null;
            String tag = null;
        	switch (id){
            case R.id.logout:
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor spEditor = sharedPreferences.edit();
                spEditor.putString("sessionKey", null);
                spEditor.putString("username", null);
                spEditor.putString("registered", null);
                spEditor.putString("playcount", null);
                spEditor.apply();
                isLogout = true;
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case (R.id.search):
                tag = "SearchFragment";
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment==null) fragment = new SearchFragment();
                break;
        	case (R.id.manual_scrobble):
                tag = "ManualScrobbleFragment";
                fragment = fragmentManager.findFragmentByTag(tag);
        		if (fragment==null) fragment = new ManualScrobbleFragment();
        		break;
        	case (R.id.scrobbles):
                tag = "ScrobblesListFragment";
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment==null) fragment = new ScrobblesListFragment();
        		break;
        	case (R.id.top_tracks):
                tag = "TopTracksFragment";
                bundle.putString("tabfragment_type", "tracks");
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment==null) fragment = new TabFragment();
        		break;
        	case (R.id.top_artists):
                tag = "TopArtistsFragment";
                bundle.putString("tabfragment_type", "artists");
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment==null) fragment = new TabFragment();
                break;
            case (R.id.top_albums):
                tag = "TopAlbumsFragment";
                bundle.putString("tabfragment_type", "albums");
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment==null) fragment = new TabFragment();
                break;
            case (R.id.settings):
                Intent intent = new Intent(this, PreferenceActivity.class);
                startActivity(intent);
                break;
            }
            try {
                //TODO переписать переключение и передачу параметров
                Fragment currentFragment = fragmentManager.findFragmentById(R.id.content_main);
                    if (currentFragment != null) {
                        if (currentFragment.getTag().equals(tag)) return true;
                        else {
                            if (fragment.getArguments() == null) fragment.setArguments(bundle);
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_main, fragment, tag);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    }
                    else {
                        if (fragment.getArguments() == null) fragment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_main, fragment, tag);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                }
            }
            catch (Exception e)
            {
                FirebaseCrash.report(e);//nullexception возникает при нажатии на logout
                e.printStackTrace();
            }
//        }
//        else {
//                Toast toast;
//                toast = Toast.makeText(getApplicationContext(), "Network is not connected!", Toast.LENGTH_SHORT);
//                toast.show();
//        }
        return true;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void CreateNavigationDrawer(){
        setContentView(R.layout.activity_main);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (!drawer.isDrawerOpen(GravityCompat.START) && !(slideOffset == 0)){
                    List fragments = getSupportFragmentManager().getFragments();
                    if (fragments != null) {
                        Fragment fragment = (Fragment) fragments.get(fragments.size() - 1);
//                        Fragment currentFragment = fragmentManager.findFragmentById(R.id.content_main);
                        if (fragment != null && fragment.getTag().equals("ManualScrobbleFragment")) {
                            ManualScrobbleFragment f = (ManualScrobbleFragment) fragment;
                            f.track.clearFocus();
                            f.artist.clearFocus();
                            f.album.clearFocus();
                            f.tracknumber.clearFocus();
                            f.trackduration.clearFocus();
                        }
                    }
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        TextView user_registered = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_registered);
        user_registered.setText(String.format("%s scrobbles since %s", playcount, registered));
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.user_name)).setText(username);
        File avatar = new File(cachepath + File.separator + "UserAvatars" + File.separator + username + ".png");
        if (avatar.exists()) {
            ImageView imageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_image);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bm = BitmapFactory.decodeFile(avatar.getPath(), options);
            imageView.setImageBitmap(bm);
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
            return info;
        }

        @Override
        protected void onPostExecute(String result){
            if (exception == 0){
                    playcount = result;
                    View nav_view = findViewById(R.id.nav_header);
                    if (nav_view != null) {
                        TextView user_registered = (TextView) nav_view.findViewById(R.id.user_registered);
                        user_registered.setText(String.format("%s scrobbles since %s", playcount, registered));
                    }
            }
            else if (exception == 1)
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            else {
                String[] exception_message = getResources().getStringArray(R.array.Exception_names);
                Toast.makeText(getApplicationContext(), exception_message[exception - 1], Toast.LENGTH_SHORT).show();
            }
        }
    }
}

