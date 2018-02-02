package by.d1makrat.library_fm.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;
import com.google.firebase.crash.FirebaseCrash;

import java.lang.reflect.Field;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.BuildConfig;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.asynctask.CheckNewVersionAsyncTask;
import by.d1makrat.library_fm.asynctask.CheckNewVersionCallback;
import by.d1makrat.library_fm.asynctask.GetUserInfoAsyncTask;
import by.d1makrat.library_fm.asynctask.GetUserInfoCallback;
import by.d1makrat.library_fm.broadcast_receiver.NetworkStateReceiver;
import by.d1makrat.library_fm.https.HttpsClient;
import by.d1makrat.library_fm.image_loader.Malevich;
import by.d1makrat.library_fm.model.User;
import by.d1makrat.library_fm.ui.fragment.ManualScrobbleFragment;
import by.d1makrat.library_fm.ui.fragment.SearchArtistFragment;
import by.d1makrat.library_fm.ui.fragment.StartFragment;
import by.d1makrat.library_fm.ui.fragment.dialog.UpdateDialogFragment;
import by.d1makrat.library_fm.ui.fragment.scrobble.RecentScrobblesFragment;
import by.d1makrat.library_fm.ui.fragment.tabTop.TabTopAlbumsFragment;
import by.d1makrat.library_fm.ui.fragment.tabTop.TabTopArtistsFragment;
import by.d1makrat.library_fm.ui.fragment.tabTop.TabTopTracksFragment;
import by.d1makrat.library_fm.utils.InputUtils;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GetUserInfoCallback, CheckNewVersionCallback {

    private static final String TAB_TOP_ALBUMS_FRAGMENT_TAG = "TabTopAlbumsFragment";
    private static final String TAB_TOP_TRACKS_FRAGMENT_TAG = "TabTopTracksFragment";
    private static final String TAB_TOP_ARTISTS_FRAGMENT_TAG = "TabTopArtistsFragment";
    private static final String UPDATE_DIALOG_FRAGMENT_TAG = "UpdateDialogFragment";
    private static final String START_FRAGMENT_TAG = "StartFragment";
    private static final String SEARCH_ARTIST_FRAGMENT_TAG = "SearchArtistFragment";
    private static final String MANUAL_SCROBBLE_FRAGMENT_TAG = "ManualScrobbleFragment";
    private static final String RECENT_SCROBBLES_FRAGMENT_TAG = "RecentScrobblesFragment";

    private FragmentManager mFragmentManager = getSupportFragmentManager();
    private User mUser;
    private BroadcastReceiver mNetworkStatusReceiver;

    @Override
    protected void onStart() {
        super.onStart();
        mNetworkStatusReceiver = new NetworkStateReceiver();
        registerReceiver(mNetworkStatusReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

        CheckNewVersionAsyncTask checkNewVersionAsyncTask = new CheckNewVersionAsyncTask(this);
        checkNewVersionAsyncTask.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-2835136610089326~9892994294");

        mUser = AppContext.getInstance().getUser();

        createView();

        mFragmentManager.beginTransaction()
                .replace(R.id.content_main, new StartFragment(), START_FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            switch (mFragmentManager.getBackStackEntryCount()) {
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        menu.removeAll();
//
//        return true;
//    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        getSupportActionBar().setSubtitle(null);
        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);

        if (HttpsClient.isNetworkAvailable()) {
            GetUserInfoAsyncTask getUserInfoAsyncTask = new GetUserInfoAsyncTask(this);
            getUserInfoAsyncTask.execute();
        }

        switch (item.getItemId()) {
            case R.id.logout:
                AppContext.getInstance().setUser(null);

                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case (R.id.search):
                showFragment(SEARCH_ARTIST_FRAGMENT_TAG, SearchArtistFragment.class);
                break;
            case (R.id.manual_scrobble):
                showFragment(MANUAL_SCROBBLE_FRAGMENT_TAG, ManualScrobbleFragment.class);
                break;
            case (R.id.scrobbles):
                getSupportActionBar().setTitle(R.string.scrobbles);
                showFragment(RECENT_SCROBBLES_FRAGMENT_TAG, RecentScrobblesFragment.class);
                break;
            case (R.id.top_tracks):
//                getSupportActionBar().setTitle(R.string.top_tracks);
                showFragment(TAB_TOP_TRACKS_FRAGMENT_TAG, TabTopTracksFragment.class);
                break;
            case (R.id.top_artists):
//                getSupportActionBar().setTitle(R.string.top_artists);
                showFragment(TAB_TOP_ARTISTS_FRAGMENT_TAG, TabTopArtistsFragment.class);
                break;
            case (R.id.top_albums):
//                getSupportActionBar().setTitle(R.string.top_albums);
                showFragment(TAB_TOP_ALBUMS_FRAGMENT_TAG, TabTopAlbumsFragment.class);
                break;
            case (R.id.settings):
                startActivity(new Intent(this, PreferenceActivity.class));
                break;
        }

        return true;
    }

    private <T extends Fragment> void showFragment(String pTag, Class<T> tClass){

        try {
            Fragment currentFragment = mFragmentManager.findFragmentById(R.id.content_main);
            if (!currentFragment.getTag().equals(pTag)) {

                Fragment targetFragment = mFragmentManager.findFragmentByTag(pTag);

                if (targetFragment == null) {
                    targetFragment = tClass.newInstance();
                }

                mFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.appear_from_right, R.anim.disappear_to_left,
                                R.anim.appear_from_left, R.anim.disappear_to_right)
                        .replace(R.id.content_main, targetFragment, pTag)
                        .addToBackStack(null)
                        .commit();
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void createView(){
        setContentView(R.layout.activity_main);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawer, (Toolbar) findViewById(R.id.toolbar), R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                InputUtils.hideKeyboard(MainActivity.this);
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
            FirebaseCrash.report(e);
        }
    }

    private void setUserInfoInHeader(User pUser) {
        View headerView = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);

        ((TextView) headerView.findViewById(R.id.user_registered)).setText(String.format("%s scrobbles since %s", pUser.getPlaycount(), pUser.getRegistered()));

        ((TextView) headerView.findViewById(R.id.user_name)).setText(pUser.getUsername());

        ImageView avatarInHeader = headerView.findViewById(R.id.nav_header_image);
        Malevich.INSTANCE.load(pUser.getAvatarUri()).onError(ContextCompat.getDrawable(this, R.drawable.img_app_logo_large)).into(avatarInHeader);
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
        unregisterReceiver(mNetworkStatusReceiver);
    }

    @Override
    public void onSuccess(Integer latestVersion) {
        if (BuildConfig.VERSION_CODE < latestVersion) {
            UpdateDialogFragment dialogFragment = new UpdateDialogFragment();
            dialogFragment.show(mFragmentManager, UPDATE_DIALOG_FRAGMENT_TAG);
        }
    }

    @Override
    public void onException(Exception exception) {
    }
}