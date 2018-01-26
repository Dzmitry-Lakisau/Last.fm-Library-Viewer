package by.d1makrat.library_fm.ui.fragment.tabTop;

import android.os.Bundle;

import by.d1makrat.library_fm.adapter.pages.TabTopTracksAdapter;

public class TabTopTracksFragment extends TabTopItemsFragment<TabTopTracksAdapter> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new TabTopTracksAdapter(getChildFragmentManager());
    }
}