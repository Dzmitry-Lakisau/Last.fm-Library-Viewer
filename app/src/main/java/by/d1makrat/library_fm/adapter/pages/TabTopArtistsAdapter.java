package by.d1makrat.library_fm.adapter.pages;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.ui.fragment.top.TopArtistsFragment;

import static by.d1makrat.library_fm.Constants.DATE_PERIODS_FOR_API;
import static by.d1makrat.library_fm.Constants.DATE_PRESETS_FOR_URL;
import static by.d1makrat.library_fm.Constants.PERIOD_KEY;

public class TabTopArtistsAdapter extends TabTopAdapter {

    public TabTopArtistsAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public TopArtistsFragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString(PERIOD_KEY, DATE_PERIODS_FOR_API[position]);
        TopArtistsFragment fragment = new TopArtistsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public Uri getUrlForBrowser(int pPosition){
        return Uri.parse(AppContext.getInstance().getUser().getUrl() + "/library/artists" + DATE_PRESETS_FOR_URL[pPosition]);
    }
}