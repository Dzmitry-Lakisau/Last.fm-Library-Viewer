package by.d1makrat.library_fm.ui.fragment.tabTop

import by.d1makrat.library_fm.adapter.pages.TabTopAlbumsAdapter

class TabTopAlbumsFragment : TabTopItemsFragment<TabTopAlbumsAdapter>() {

    override fun createAdapter(): TabTopAlbumsAdapter {
        return TabTopAlbumsAdapter(childFragmentManager)
    }
}
