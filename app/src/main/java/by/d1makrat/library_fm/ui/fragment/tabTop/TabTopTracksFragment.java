package by.d1makrat.library_fm.ui.fragment.tabTop;

import by.d1makrat.library_fm.adapter.pages.TabTopTracksAdapter;

public class TabTopTracksFragment extends TabTopItemsFragment<TabTopTracksAdapter> {

    @Override
    protected TabTopTracksAdapter createAdapter() {
        return new TabTopTracksAdapter(getChildFragmentManager());
    }
}
