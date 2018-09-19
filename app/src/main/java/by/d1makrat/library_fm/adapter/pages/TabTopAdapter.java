package by.d1makrat.library_fm.adapter.pages;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import by.d1makrat.library_fm.model.Period;
import by.d1makrat.library_fm.ui.fragment.ItemsFragment;

public abstract class TabTopAdapter extends FragmentPagerAdapter {

    TabTopAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return new Period().getSize();
    }

    @Override
    public String getPageTitle(int position) {
        return new Period().getName(position);
    }

    public abstract Uri getUrlForBrowser(int position);

    public abstract ItemsFragment getItem(int position);
}
