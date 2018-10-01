package by.d1makrat.library_fm.view.activity

import by.d1makrat.library_fm.model.User

interface MainView {
    fun setUserInfoInHeader(user: User)
    fun setUpActionBar(title: String, subtitle: String? = null)
    fun showScrobblesOfArtistFragment(artist: String)
    fun showScrobblesOfTrackFragment(artist: String, track: String)
    fun showScrobblesOfAlbumFragment(artist: String, album: String)
    fun showScrobblesOfDay(startOfDay: Long, endOfDay: Long)
    fun showAboutDialog()
    fun showUpdateDialog()
}
