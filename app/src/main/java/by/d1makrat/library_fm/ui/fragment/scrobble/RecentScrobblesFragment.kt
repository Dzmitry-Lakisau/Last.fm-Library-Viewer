package by.d1makrat.library_fm.ui.fragment.scrobble

import android.os.Bundle
import by.d1makrat.library_fm.presenter.fragment.scrobble.RecentScrobblesPresenter

class RecentScrobblesFragment : ScrobblesFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = RecentScrobblesPresenter(filterRange)
    }
}
