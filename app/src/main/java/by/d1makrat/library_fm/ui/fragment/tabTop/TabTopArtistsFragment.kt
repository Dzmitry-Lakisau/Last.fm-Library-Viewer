package by.d1makrat.library_fm.ui.fragment.tabTop

import by.d1makrat.library_fm.adapter.pages.TabTopArtistsAdapter

class TabTopArtistsFragment : TabTopItemsFragment<TabTopArtistsAdapter>() {

    override fun createAdapter(): TabTopArtistsAdapter {
        return TabTopArtistsAdapter(childFragmentManager)
    }
}
