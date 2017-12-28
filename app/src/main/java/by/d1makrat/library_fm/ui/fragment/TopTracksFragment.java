package by.d1makrat.library_fm.ui.fragment;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.adapter.list.RankedListAdapter;
import by.d1makrat.library_fm.asynctask.GetUserTopTracksAsynctask;

import static by.d1makrat.library_fm.Constants.PERIOD_KEY;

public class TopTracksFragment extends ScrobblesListFragment {

    private String mPeriod;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPeriod = getArguments().getString(PERIOD_KEY);
        urlForBrowser = AppContext.getInstance().getUser().getUrl() + "/library/tracks/";
    }

    @Override
    protected BaseAdapter createAdapter(){

        Drawable drawable;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = getResources().getDrawable(R.drawable.default_albumart);
        } else
            drawable = getResources().getDrawable(R.drawable.default_albumart, null);

        return new RankedListAdapter(getActivity().getLayoutInflater(), drawable, mRankedItems);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.top_tracks);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }

    @Override
    public void loadItemsFromWeb() {
        String[] asynctaskArgs = {mPeriod, String.valueOf(mPage)};
        mGetItemsAsynctask = new GetUserTopTracksAsynctask(this);
        mGetItemsAsynctask.execute(asynctaskArgs);
    }
}