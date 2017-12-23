package by.d1makrat.library_fm.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;

import java.lang.reflect.Field;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.GetUserInfoAsynctaskCallback;
import by.d1makrat.library_fm.NetworkStatusChecker;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.asynctask.GetUserInfoAsynctask;
import by.d1makrat.library_fm.image_loader.Malevich;
import by.d1makrat.library_fm.model.User;
import by.d1makrat.library_fm.ui.fragment.ManualScrobbleFragment;
import by.d1makrat.library_fm.ui.fragment.RecentScrobblesFragment;
import by.d1makrat.library_fm.ui.fragment.SearchArtistFragment;
import by.d1makrat.library_fm.ui.fragment.StartFragment;
import by.d1makrat.library_fm.ui.fragment.TabFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GetUserInfoAsynctaskCallback {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-2835136610089326~9892994294");

        mUser = AppContext.getInstance().getUser();

        createView();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        StartFragment fragment = new StartFragment();
        fragmentTransaction.replace(R.id.content_main, fragment, "StartFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
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

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        getSupportActionBar().setSubtitle(null);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        int id = item.getItemId();
        Bundle bundle = new Bundle();

        if (NetworkStatusChecker.isNetworkAvailable()) {
            GetUserInfoAsynctask getUserInfoAsynctask = new GetUserInfoAsynctask(this);
            getUserInfoAsynctask.execute();
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
                AppContext.getInstance().setUser(null);

                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case (R.id.search):
                tag = "SearchFragment";
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment==null) fragment = new SearchArtistFragment();
                break;
        	case (R.id.manual_scrobble):
                tag = "ManualScrobbleFragment";
                fragment = fragmentManager.findFragmentByTag(tag);
        		if (fragment==null) fragment = new ManualScrobbleFragment();
        		break;
        	case (R.id.scrobbles):
                tag = "RecentScrobblesFragment";
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment==null) fragment = new RecentScrobblesFragment();
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
                //FirebaseCrash.report(e);//nullexception возникает при нажатии на logout
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

    private void createView(){
        setContentView(R.layout.activity_main);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        setUserInfoInHeader(mUser);

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUserInfoInHeader(User pUser) {
        View headerView = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);

        ((TextView) headerView.findViewById(R.id.user_registered)).setText(String.format("%s scrobbles since %s", pUser.getPlaycount(), pUser.getRegistered()));

        ((TextView) headerView.findViewById(R.id.user_name)).setText(pUser.getUsername());

        ImageView avatarInHeader = headerView.findViewById(R.id.nav_header_image);
        Malevich.INSTANCE.load(pUser.getAvatarUri()).into(avatarInHeader);
    }

    @Override
    public void onUserInfoReceived(User user) {
        AppContext.getInstance().setUser(user);
        setUserInfoInHeader(user);
    }

    @Override
    protected void onStop() {
        super.onStop();

        AppContext.getInstance().saveSettings();
    }

    @Override
    public void onException(Exception exception) {
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}

