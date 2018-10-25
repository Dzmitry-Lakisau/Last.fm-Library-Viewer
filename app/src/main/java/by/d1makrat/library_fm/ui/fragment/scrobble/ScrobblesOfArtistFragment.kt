package by.d1makrat.library_fm.ui.fragment.scrobble

import android.os.Bundle
import android.view.ContextMenu
import android.view.View
import by.d1makrat.library_fm.Constants
import by.d1makrat.library_fm.R
import by.d1makrat.library_fm.presenter.fragment.scrobble.ScrobblesOfArtistPresenter

class ScrobblesOfArtistFragment: ScrobblesFragment() {

    private var artist: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        artist = arguments!!.getString(Constants.ARTIST_KEY)

        presenter = ScrobblesOfArtistPresenter(artist!!, filterRange)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        menu?.findItem(R.id.scrobbles_of_artist)?.isVisible = false
    }
}
