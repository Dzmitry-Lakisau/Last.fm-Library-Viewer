package by.d1makrat.library_fm.ui.fragment.tabTop

import by.d1makrat.library_fm.adapter.pages.TabTopTracksAdapter

class TabTopTracksFragment : TabTopItemsFragment<TabTopTracksAdapter>() {

    override fun createAdapter(): TabTopTracksAdapter {
        return TabTopTracksAdapter(childFragmentManager)
    }
}
