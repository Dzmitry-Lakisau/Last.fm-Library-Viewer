package by.d1makrat.library_fm.view.activity

import android.support.v4.app.Fragment
import by.d1makrat.library_fm.model.FilterRange
import by.d1makrat.library_fm.model.User

interface MainView {
    fun setUpNavigationItemChecked(fragment: Fragment?)
    fun setUserInfoInHeader(user: User)
    fun setUpTitle(fragment: Fragment?)
    fun openScrobblesOfArtistFragment(artist: String)
    fun openScrobblesOfTrackFragment(artist: String, track: String)
    fun openScrobblesOfAlbumFragment(artist: String, album: String)
    fun openScrobblesFragment(filterRange: FilterRange = FilterRange(null, null))
    fun showAboutDialog()
}
