package by.d1makrat.library_fm.ui.fragment.tabTop;

import android.os.Bundle;

import by.d1makrat.library_fm.adapter.pages.TabTopAlbumsAdapter;

public class TabTopAlbumsFragment extends TabTopItemsFragment<TabTopAlbumsAdapter> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new TabTopAlbumsAdapter(getChildFragmentManager());
    }
}