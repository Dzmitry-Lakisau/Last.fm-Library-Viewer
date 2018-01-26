package by.d1makrat.library_fm.adapter.pages;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.ui.fragment.top.TopTracksFragment;

import static by.d1makrat.library_fm.Constants.DATE_PERIODS_FOR_API;
import static by.d1makrat.library_fm.Constants.DATE_PRESETS_FOR_URL;
import static by.d1makrat.library_fm.Constants.PERIOD_KEY;

public class TabTopTracksAdapter extends TabTopAdapter {

    public TabTopTracksAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public TopTracksFragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString(PERIOD_KEY, DATE_PERIODS_FOR_API[position]);
        TopTracksFragment fragment = new TopTracksFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public Uri getUrlForBrowser(int pPosition){
        return Uri.parse(AppContext.getInstance().getUser().getUrl() + "/library/tracks" + DATE_PRESETS_FOR_URL[pPosition]);
    }
}