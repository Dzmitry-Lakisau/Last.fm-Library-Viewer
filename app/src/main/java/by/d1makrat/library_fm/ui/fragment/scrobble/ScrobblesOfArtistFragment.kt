package by.d1makrat.library_fm.ui.fragment.scrobble

import android.os.Bundle
import android.view.ContextMenu
import android.view.View
import by.d1makrat.library_fm.Constants.ARTIST_KEY
import by.d1makrat.library_fm.R
import by.d1makrat.library_fm.presenter.fragment.scrobble.ScrobblesOfArtistPresenter

class ScrobblesOfArtistFragment: ScrobblesFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = ScrobblesOfArtistPresenter(arguments?.getString(ARTIST_KEY)!!, filterRange)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        menu?.findItem(R.id.scrobbles_of_artist)?.isVisible = false
    }
}
