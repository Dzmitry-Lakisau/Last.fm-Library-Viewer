package by.d1makrat.library_fm.ui.fragment.tabTop;

import android.os.Bundle;

import by.d1makrat.library_fm.adapter.pages.TabTopArtistsAdapter;

public class TabTopArtistsFragment extends TabTopItemsFragment<TabTopArtistsAdapter> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new TabTopArtistsAdapter(getChildFragmentManager());
    }
}