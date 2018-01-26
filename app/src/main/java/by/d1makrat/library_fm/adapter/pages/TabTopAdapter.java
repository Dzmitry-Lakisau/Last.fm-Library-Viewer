package by.d1makrat.library_fm.adapter.pages;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import by.d1makrat.library_fm.ui.fragment.ItemsFragment;

import static by.d1makrat.library_fm.Constants.DATE_PERIODS_FOR_API;
import static by.d1makrat.library_fm.Constants.DATE_PERIODS_FOR_TAB_NAMES;

public abstract class TabTopAdapter extends FragmentPagerAdapter {

    TabTopAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return(DATE_PERIODS_FOR_API.length);
    }

    @Override
    public String getPageTitle(int position) {
        return DATE_PERIODS_FOR_TAB_NAMES[position];
    }

    public abstract Uri getUrlForBrowser(int position);

    public abstract ItemsFragment getItem(int position);
}