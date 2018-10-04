package by.d1makrat.library_fm.view.activity

import android.support.v4.app.Fragment
import by.d1makrat.library_fm.Constants.DATE_LONG_DEFAULT_VALUE
import by.d1makrat.library_fm.model.User

interface MainView {
    fun setUpNavigationItemChecked(fragment: Fragment?)
    fun setUserInfoInHeader(user: User)
    fun setUpTitle(fragment: Fragment?)
    fun openScrobblesOfArtistFragment(artist: String)
    fun openScrobblesOfTrackFragment(artist: String, track: String)
    fun openScrobblesOfAlbumFragment(artist: String, album: String)
    fun openScrobblesFragment(startOfPeriod: Long = DATE_LONG_DEFAULT_VALUE, endOfPeriod: Long = DATE_LONG_DEFAULT_VALUE)
    fun showAboutDialog()
}
