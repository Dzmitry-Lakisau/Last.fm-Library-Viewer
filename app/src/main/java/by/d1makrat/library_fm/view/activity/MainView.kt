package by.d1makrat.library_fm.view.activity

import android.support.v4.app.Fragment
import by.d1makrat.library_fm.model.User

interface MainView {
    fun setUpNavigationItemChecked(fragment: Fragment?)
    fun setUserInfoInHeader(user: User)
    fun setUpTitle(fragment: Fragment?)
    fun showScrobblesOfArtistFragment(artist: String)
    fun showScrobblesOfTrackFragment(artist: String, track: String)
    fun showScrobblesOfAlbumFragment(artist: String, album: String)
    fun showScrobblesOfDay(startOfDay: Long, endOfDay: Long)
    fun showAboutDialog()
}
