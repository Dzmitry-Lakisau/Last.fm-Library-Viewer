package by.d1makrat.library_fm.ui.fragment.tabTop;

import by.d1makrat.library_fm.adapter.pages.TabTopArtistsAdapter;

public class TabTopArtistsFragment extends TabTopItemsFragment<TabTopArtistsAdapter> {

    @Override
    protected TabTopArtistsAdapter createAdapter() {
        return new TabTopArtistsAdapter(getChildFragmentManager());
    }
}
