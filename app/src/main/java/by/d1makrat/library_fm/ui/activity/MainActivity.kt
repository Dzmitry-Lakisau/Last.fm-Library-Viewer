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
import by.d1makrat.library_fm.R.string.app_name
import by.d1makrat.library_fm.broadcast_receiver.NetworkStateReceiver
import by.d1makrat.library_fm.image_loader.Malevich
import by.d1makrat.library_fm.model.FilterRange
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
import com.crashlytics.android.Crashlytics
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

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
            showNewFragment(getString(app_name), StartFragment(), false)
        }
        else {
            val currentFragment = supportFragmentManager.fragments[0]
            setUpTitle(currentFragment)
            setUpNavigationItemChecked(currentFragment)
        }
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
            Crashlytics.logException(e)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        (findViewById<View>(R.id.drawer_layout) as DrawerLayout).closeDrawer(GravityCompat.START)

        presenter.onNavigationItemSelected()

        when (item.itemId) {
            R.id.scrobbles -> {
                openScrobblesFragment()
            }
            R.id.top_tracks -> {
                showNewFragment(TAB_TOP_TRACKS_FRAGMENT_TAG, TabTopTracksFragment(), true)
            }
            R.id.top_artists -> {
                showNewFragment(TAB_TOP_ARTISTS_FRAGMENT_TAG, TabTopArtistsFragment(),true)
            }
            R.id.top_albums -> {
                showNewFragment(TAB_TOP_ALBUMS_FRAGMENT_TAG, TabTopAlbumsFragment(),true)
            }
            R.id.search -> {
                showNewFragment(SEARCH_ARTIST_FRAGMENT_TAG, SearchArtistFragment(), true)
            }
            R.id.manual_scrobble -> {
                showNewFragment(MANUAL_SCROBBLE_FRAGMENT_TAG, ManualScrobbleFragment(), true)
            }
            R.id.settings -> startActivity(Intent(this, PreferenceActivity::class.java))
            R.id.logout -> {
                presenter.onLogout()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        item.isChecked = true

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
                    setUpTitle(fragment)
                    setUpNavigationItemChecked(fragment)
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

    override fun setUpTitle(fragment: Fragment?) {
        when (fragment) {
            is ScrobblesOfAlbumFragment -> {
                supportActionBar?.title = fragment.arguments?.getString(ARTIST_KEY)!!
                supportActionBar?.subtitle = fragment.arguments?.getString(ALBUM_KEY)!!
            }
            is ScrobblesOfArtistFragment -> {
                supportActionBar?.title = fragment.arguments?.getString(ARTIST_KEY)!!
                supportActionBar?.subtitle = null
            }
            is ScrobblesOfTrackFragment -> {
                supportActionBar?.title = fragment.arguments?.getString(ARTIST_KEY)!!
                supportActionBar?.subtitle = fragment.arguments?.getString(TRACK_KEY)!!
            }
            else -> {
                supportActionBar?.title = fragment?.tag
                supportActionBar?.subtitle = null
            }
        }
    }

    override fun setUpNavigationItemChecked(fragment: Fragment?){
        val navigationView = drawer_layout.nav_view
        when (fragment) {
            is TabTopAlbumsFragment -> navigationView.setCheckedItem(R.id.top_albums)
            is TabTopArtistsFragment -> navigationView.setCheckedItem(R.id.top_artists)
            is TabTopTracksFragment -> navigationView.setCheckedItem(R.id.top_tracks)
            is SearchArtistFragment -> navigationView.setCheckedItem(R.id.search)
            is ManualScrobbleFragment -> navigationView.setCheckedItem(R.id.manual_scrobble)
            is StartFragment -> {
                val navMenu = navigationView.menu
                for (i in 0 until navMenu.size()) {
                    navMenu.getItem(i).isChecked = false
                }
            }
            else -> {
                navigationView.setCheckedItem(R.id.scrobbles)
            }
        }
    }

    override fun openScrobblesOfArtistFragment(artist: String){
        val targetFragment = ScrobblesOfArtistFragment()
        val bundle = Bundle()
        bundle.putString(ARTIST_KEY, artist)
        targetFragment.arguments = bundle
        showNewFragment(SCROBBLES_OF_ARTIST_TAG, targetFragment, true)
    }

    override fun openScrobblesOfTrackFragment(artist: String, track: String) {
        val targetFragment = ScrobblesOfTrackFragment()
        val bundle = Bundle()
        bundle.putString(ARTIST_KEY, artist)
        bundle.putString(TRACK_KEY, track)
        targetFragment.arguments = bundle
        showNewFragment(SCROBBLES_OF_TRACK_TAG, targetFragment,true)
    }

    override fun openScrobblesOfAlbumFragment(artist: String, album: String) {
        val targetFragment = ScrobblesOfAlbumFragment()
        val bundle = Bundle()
        bundle.putString(ARTIST_KEY, artist)
        bundle.putString(ALBUM_KEY, album)
        targetFragment.arguments = bundle
        showNewFragment(SCROBBLES_OF_ALBUM_TAG, targetFragment, true)
    }

    override fun openScrobblesFragment(filterRange: FilterRange) {
        val targetFragment = RecentScrobblesFragment()
        val bundle = Bundle()
        bundle.putParcelable(FilterRange::class.java.simpleName, filterRange)
        targetFragment.arguments = bundle
        showNewFragment(RECENT_SCROBBLES_FRAGMENT_TAG, targetFragment, true)
    }

    override fun showAboutDialog(){
        val dialogFragment = AboutDialogFragment()
        dialogFragment.show(supportFragmentManager, ABOUT_DIALOG_FRAGMENT_TAG)
    }

    private fun <T: Fragment> showNewFragment(tag: String, targetFragment: T, showAnimation: Boolean) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.content_main)

        if (currentFragment == null || currentFragment.tag != tag) {
            if (showAnimation) {
                supportFragmentManager.beginTransaction()
                        .setCustomAnimations(appear_from_right, disappear_to_left,
                                appear_from_left, disappear_to_right)
                        .replace(R.id.content_main, targetFragment, tag)
                        .addToBackStack(tag)
                        .commit()
            } else {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.content_main, targetFragment, tag)
                        .addToBackStack(tag)
                        .commit()
            }

            setUpTitle(targetFragment)
            setUpNavigationItemChecked(targetFragment)
        }
    }
}
