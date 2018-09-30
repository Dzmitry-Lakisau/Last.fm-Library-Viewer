package by.d1makrat.library_fm.ui.fragment.scrobble

import android.os.Bundle
import by.d1makrat.library_fm.Constants.*
import by.d1makrat.library_fm.presenter.fragment.scrobble.RecentScrobblesPresenter

class RecentScrobblesFragment : ScrobblesFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mFrom = arguments?.getLong(FILTER_DIALOG_FROM_BUNDLE_KEY, DATE_LONG_DEFAUT_VALUE) ?: DATE_LONG_DEFAUT_VALUE
        mTo = arguments?.getLong(FILTER_DIALOG_TO_BUNDLE_KEY, DATE_LONG_DEFAUT_VALUE) ?: DATE_LONG_DEFAUT_VALUE

        presenter = RecentScrobblesPresenter(mFrom, mTo)
    }
}
