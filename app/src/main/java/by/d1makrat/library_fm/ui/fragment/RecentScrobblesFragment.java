package by.d1makrat.library_fm.ui.fragment;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.adapter.list.ScrobblesListAdapter;
import by.d1makrat.library_fm.asynctask.GetRecentScrobblesAsynctask;

public class RecentScrobblesFragment extends ScrobblesListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUrlForBrowser = AppContext.getInstance().getUser().getUrl() + "/library/";
    }

    @Override
    protected BaseAdapter createAdapter(){

        //TODO ContextCompat.getDrawable()
        Drawable drawable;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = getResources().getDrawable(R.drawable.default_albumart);
        } else
            drawable = getResources().getDrawable(R.drawable.default_albumart, null);

        return new ScrobblesListAdapter(getActivity().getLayoutInflater(), drawable, mScrobbles);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_scrobbles, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.scrobbles);

        return rootView;
    }

    @Override
    protected void loadItemsFromWeb() {
        String[] asynctaskArgs = {String.valueOf(mPage), mFrom, mTo};
        mGetItemsAsynctask = new GetRecentScrobblesAsynctask(this);
        mGetItemsAsynctask.execute(asynctaskArgs);
    }
}