package by.d1makrat.library_fm.ui.fragment.scrobble

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ContextMenu
import android.view.View
import by.d1makrat.library_fm.Constants.ALBUM_KEY
import by.d1makrat.library_fm.Constants.ARTIST_KEY
import by.d1makrat.library_fm.R
import by.d1makrat.library_fm.presenter.fragment.scrobble.ScrobblesOfAlbumPresenter

class ScrobblesOfAlbumFragment : ScrobblesFragment() {

    private var artist: String? = null
    private var album: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        artist = arguments!!.getString(ARTIST_KEY)
        album = arguments!!.getString(ALBUM_KEY)

        presenter = ScrobblesOfAlbumPresenter(artist!!, album!!, mFrom, mTo)
        presenter?.loadItems()
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        menu?.findItem(R.id.scrobbles_of_album)?.isVisible = false
    }

    override fun setUpActionBar(activity: AppCompatActivity?) {
        val actionBar = activity!!.supportActionBar
        if (actionBar != null) {
            actionBar.title = artist
            actionBar.subtitle = album
        }
    }
}
