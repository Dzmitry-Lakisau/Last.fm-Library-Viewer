package by.d1makrat.library_fm.ui.fragment;

import android.os.Bundle;

import by.d1makrat.library_fm.asynctask.GetUserTopAlbumsAsynctask;

import static by.d1makrat.library_fm.Constants.PERIOD_KEY;

public class TopAlbumsFragment extends ScrobblesListFragment{

    private String mPeriod;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPeriod = getArguments().getString(PERIOD_KEY);
    }

    @Override
    public void loadItemsFromWeb() {
        mGetScrobblesAsynctask = new GetUserTopAlbumsAsynctask(this);
        mGetScrobblesAsynctask.execute(mPeriod, String.valueOf(mPage));
    }
}