package by.d1makrat.library_fm.adapter.pages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import by.d1makrat.library_fm.ui.fragment.TopArtistsFragment;

public class TopArtistsAdapter extends FragmentPagerAdapter {
    private final String periods[] = new String[]{"overall", "7day", "1month", "3month", "6month",  "12month"};
    private final String tab_names[] = new String[]{"Overall", "Week", "Month", "3 months", "6 months", "Year"};

    public TopArtistsAdapter(FragmentManager mgr) {
        super(mgr);
    }

    @Override
    public int getCount() {
        return(periods.length);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("period", periods[position]);
        Fragment fragment = new TopArtistsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public String getPageTitle(int position) {
        return tab_names[position];
    }
}