package by.d1makrat.library_fm.ui.fragment.tabTop;

import by.d1makrat.library_fm.adapter.pages.TabTopAlbumsAdapter;

public class TabTopAlbumsFragment extends TabTopItemsFragment<TabTopAlbumsAdapter> {

    @Override
    protected TabTopAlbumsAdapter createAdapter() {
        return new TabTopAlbumsAdapter(getChildFragmentManager());
    }
}
