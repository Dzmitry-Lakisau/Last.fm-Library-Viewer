package by.d1makrat.library_fm.ui.fragment.scrobble

import android.os.Bundle
import android.view.ContextMenu
import android.view.View
import by.d1makrat.library_fm.Constants.ARTIST_KEY
import by.d1makrat.library_fm.Constants.TRACK_KEY
import by.d1makrat.library_fm.R
import by.d1makrat.library_fm.presenter.fragment.scrobble.ScrobblesOfTrackPresenter

class ScrobblesOfTrackFragment: ScrobblesFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = ScrobblesOfTrackPresenter(arguments?.getString(ARTIST_KEY)!!, arguments?.getString(TRACK_KEY)!!, filterRange)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        menu?.findItem(R.id.scrobbles_of_track)?.isVisible = false
    }
}
