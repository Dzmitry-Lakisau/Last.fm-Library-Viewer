package by.d1makrat.library_fm.adapter.pages;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.model.Period;
import by.d1makrat.library_fm.ui.fragment.top.TopAlbumsFragment;

import static by.d1makrat.library_fm.Constants.PERIOD_KEY;

public class TabTopAlbumsAdapter extends TabTopAdapter {

    public TabTopAlbumsAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public TopAlbumsFragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString(PERIOD_KEY, new Period().getValueForApi(position));
        TopAlbumsFragment fragment = new TopAlbumsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public Uri getUrlForBrowser(int pPosition){
        return Uri.parse(AppContext.getInstance().getUser().getUrl() + "/library/albums" + new Period().getSuffixForUrl(pPosition));
    }
}
