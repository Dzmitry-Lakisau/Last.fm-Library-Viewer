package by.d1makrat.library_fm.ui.activity

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.view.ViewConfiguration
import android.widget.ImageView
import android.widget.TextView
import by.d1makrat.library_fm.Constants.*
import by.d1makrat.library_fm.R
import by.d1makrat.library_fm.R.anim.*
import by.d1makrat.library_fm.R.string.*
import by.d1makrat.library_fm.broadcast_receiver.NetworkStateReceiver
import by.d1makrat.library_fm.image_loader.Malevich
import by.d1makrat.library_fm.model.User
import by.d1makrat.library_fm.presenter.activity.MainPresenter
import by.d1makrat.library_fm.ui.fragment.ManualScrobbleFragment
import by.d1makrat.library_fm.ui.fragment.SearchArtistFragment
import by.d1makrat.library_fm.ui.fragment.StartFragment
import by.d1makrat.library_fm.ui.fragment.dialog.AboutDialogFragment
import by.d1makrat.library_fm.ui.fragment.scrobble.RecentScrobblesFragment
import by.d1makrat.library_fm.ui.fragment.scrobble.ScrobblesOfAlbumFragment
import by.d1makrat.library_fm.ui.fragment.scrobble.ScrobblesOfArtistFragment
import by.d1makrat.library_fm.ui.fragment.scrobble.ScrobblesOfTrackFragment
import by.d1makrat.library_fm.ui.fragment.tabTop.TabTopAlbumsFragment
import by.d1makrat.library_fm.ui.fragment.tabTop.TabTopArtistsFragment
import by.d1makrat.library_fm.ui.fragment.tabTop.TabTopTracksFragment
import by.d1makrat.library_fm.utils.InputUtils
import by.d1makrat.library_fm.view.activity.MainView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.crash.FirebaseCrash

class MainActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, MainView {

    private val ABOUT_DIALOG_FRAGMENT_TAG = "AboutDialogFragment"
    private val TAB_TOP_ALBUMS_FRAGMENT_TAG = "Top Albums"
    private val TAB_TOP_TRACKS_FRAGMENT_TAG = "Top Tracks"
    private val TAB_TOP_ARTISTS_FRAGMENT_TAG = "Top Artists"
    private val SEARCH_ARTIST_FRAGMENT_TAG = "Search Artist"
    private val MANUAL_SCROBBLE_FRAGMENT_TAG = "Manual Scrobble"

    private val ADMOB_APP_ID = "ca-app-pub-2835136610089326~9892994294"

    private var presenter = MainPresenter()

    private var mNetworkStatusReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MobileAds.initialize(applicationContext, ADMOB_APP_ID)

        createView()

        if (supportFragmentManager.backStackEntryCount == 0) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.content_main, StartFragment(), getString(app_name))
                    .addToBackStack(getString(app_name))
                    .commit()
        }
        setUpActionBar(getString(app_name))
    }

    override fun onStart() {
        super.onStart()

        presenter.attachView(this)

        mNetworkStatusReceiver = NetworkStateReceiver()
        registerReceiver(mNetworkStatusReceiver, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
    }

    override fun onStop() {
        super.onStop()

        presenter.detachView()

        unregisterReceiver(mNetworkStatusReceiver)
    }

    private fun createView() {
        setContentView(R.layout.activity_main)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        setSupportActionBar(findViewById<View>(R.id.toolbar) as Toolbar)
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = object : ActionBarDrawerToggle(this, drawer, findViewById<View>(R.id.toolbar) as Toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                InputUtils.hideKeyboard(this@MainActivity)
            }
        }
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        try {
            val config = ViewConfiguration.get(this)
            val menuKeyField = ViewConfiguration::class.java.getDeclaredField("sHasPermanentMenuKey")
            if (menuKeyField != null) {
                menuKeyField.isAccessible = true
                menuKeyField.setBoolean(config, false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            FirebaseCrash.report(e)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

//        if (supportActionBar != null) supportActionBar!!.setSubtitle(null)
        (findViewById<View>(R.id.drawer_layout) as DrawerLayout).closeDrawer(GravityCompat.START)

        presenter.onNavigationItemSelected()

        when (item.itemId) {
            R.id.scrobbles -> {
                showFragment(RECENT_SCROBBLES_FRAGMENT_TAG, RecentScrobblesFragment())
                setUpActionBar(getString(R.string.scrobbles))
            }
            R.id.top_tracks -> {
                showFragment(TAB_TOP_TRACKS_FRAGMENT_TAG, TabTopTracksFragment())
                setUpActionBar(getString(top_tracks))
            }
            R.id.top_artists -> {
                showFragment(TAB_TOP_ARTISTS_FRAGMENT_TAG, TabTopArtistsFragment())
                setUpActionBar(getString(top_artists))
            }
            R.id.top_albums -> {
                showFragment(TAB_TOP_ALBUMS_FRAGMENT_TAG, TabTopAlbumsFragment())
                setUpActionBar(getString(top_albums))
            }
            R.id.search -> {
                showFragment(SEARCH_ARTIST_FRAGMENT_TAG, SearchArtistFragment())
                setUpActionBar(getString(search_artist))
            }
            R.id.manual_scrobble -> {
                showFragment(MANUAL_SCROBBLE_FRAGMENT_TAG, ManualScrobbleFragment())
                setUpActionBar(getString(R.string.manual_scrobble))
            }
            R.id.settings -> startActivity(Intent(this, PreferenceActivity::class.java))
            R.id.logout -> {
                presenter.onLogout()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        return true
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            when (supportFragmentManager.backStackEntryCount) {
                1 -> finish()
                else -> {
                    val fragment = supportFragmentManager.findFragmentByTag(
                            supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 2).name
                    )
                    when (fragment) {
                        is ScrobblesOfAlbumFragment -> setUpActionBar(fragment.arguments?.getString(ARTIST_KEY)!!, fragment.arguments?.getString(ALBUM_KEY)!!)
                        is ScrobblesOfArtistFragment -> setUpActionBar(fragment.arguments?.getString(ARTIST_KEY)!!)
                        is ScrobblesOfTrackFragment -> setUpActionBar(fragment.arguments?.getString(ARTIST_KEY)!!, fragment.arguments?.getString(TRACK_KEY)!!)
                        else -> setUpActionBar(fragment.tag!!)
                    }
                    super.onBackPressed()
                }
            }
        }
    }

    override fun setUserInfoInHeader(user: User) {
        val headerView = (findViewById<View>(R.id.nav_view) as NavigationView).getHeaderView(0)

        (headerView.findViewById<View>(R.id.user_registered) as TextView).text = getString(R.string.navigation_drawer_header_message, user.playcount, user.registered)

        (headerView.findViewById<View>(R.id.user_name) as TextView).text = user.username

        val avatarInHeader = headerView.findViewById<ImageView>(R.id.nav_header_image)
        Malevich.INSTANCE.load(user.avatarUrl).onError(ContextCompat.getDrawable(this, R.drawable.img_app_logo_large)).into(avatarInHeader)
    }

    override fun setUpActionBar(title: String, subtitle: String?) {
        supportActionBar?.title = title
        supportActionBar?.subtitle = subtitle
    }

    override fun showScrobblesOfArtistFragment(artist: String){
        val targetFragment = ScrobblesOfArtistFragment()
        val bundle = Bundle()
        bundle.putString(ARTIST_KEY, artist)
        targetFragment.arguments = bundle
        showFragment(SCROBBLES_OF_ARTIST_TAG, targetFragment)

        setUpActionBar(artist)
    }

    override fun showScrobblesOfTrackFragment(artist: String, track: String) {
        val targetFragment = ScrobblesOfTrackFragment()
        val bundle = Bundle()
        bundle.putString(ARTIST_KEY, artist)
        bundle.putString(TRACK_KEY, track)
        targetFragment.arguments = bundle
        showFragment(SCROBBLES_OF_TRACK_TAG, targetFragment)

        setUpActionBar(artist, track)
    }

    override fun showScrobblesOfAlbumFragment(artist: String, album: String) {
        val targetFragment = ScrobblesOfAlbumFragment()
        val bundle = Bundle()
        bundle.putString(ARTIST_KEY, artist)
        bundle.putString(ALBUM_KEY, album)
        targetFragment.arguments = bundle
        showFragment(SCROBBLES_OF_ALBUM_TAG, targetFragment)

        setUpActionBar(artist, album)
    }

    override fun showScrobblesOfDay(startOfDay: Long, endOfDay: Long) {
        val targetFragment = RecentScrobblesFragment()
        val bundle = Bundle()
        bundle.putLong(FILTER_DIALOG_FROM_BUNDLE_KEY, startOfDay)
        bundle.putLong(FILTER_DIALOG_TO_BUNDLE_KEY, endOfDay)
        targetFragment.arguments = bundle
        showFragment(RECENT_SCROBBLES_FRAGMENT_TAG, targetFragment)

        setUpActionBar(getString(R.string.scrobbles))
    }

    override fun showAboutDialog(){
        val dialogFragment = AboutDialogFragment()
        dialogFragment.show(supportFragmentManager, ABOUT_DIALOG_FRAGMENT_TAG)
    }

    private fun <T: Fragment> showFragment(tag: String, targetFragment: T){
        val fragmentFromBackStack = supportFragmentManager.findFragmentByTag(tag)

        if (fragmentFromBackStack != null){
            supportFragmentManager.beginTransaction()
                    .setCustomAnimations(appear_from_right, disappear_to_left,
                            appear_from_left, disappear_to_right)
                    .replace(R.id.content_main, fragmentFromBackStack, tag)
                    .addToBackStack(tag)
                    .commit()
        }
        else {
            supportFragmentManager.beginTransaction()
                    .setCustomAnimations(appear_from_right, disappear_to_left,
                            appear_from_left, disappear_to_right)
                    .replace(R.id.content_main, targetFragment, tag)
                    .addToBackStack(tag)
                    .commit()
        }
    }
}
