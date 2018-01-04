package by.d1makrat.library_fm.ui.fragment;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.adapter.list.ScrobblesListAdapter;
import by.d1makrat.library_fm.asynctask.GetScrobblesOfTrackAsynctask;

public class ScrobblesOfTrackFragment extends ScrobblesListFragment {

    private static final String ARTIST_BUNDLE_KEY = "artist";
    private static final java.lang.String TRACK_BUNDLE_KEY = "track";
    private static String artist;
    private static String track;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        artist = getArguments().getString(ARTIST_BUNDLE_KEY);
        track = getArguments().getString(TRACK_BUNDLE_KEY);
        mUrlForBrowser = AppContext.getInstance().getUser().getUrl() + "/library/music/" + artist + "/_/" + track;
    }

    @Override
    protected BaseAdapter createAdapter(){

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

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(artist);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(track);

        return rootView;
    }
//TODO check behaviour for limit>200

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(contextMenu, v ,menuInfo);

        contextMenu.getItem(1).setVisible(false);
    }

    @Override
    public void loadItemsFromWeb() {
        String[] asynctaskArgs = {artist, track, mFrom, mTo};
        mGetItemsAsynctask = new GetScrobblesOfTrackAsynctask(this);
        mGetItemsAsynctask.execute(asynctaskArgs);
    }

    @Override
    public void onScroll(AbsListView l, int firstVisibleItem, int visibleItemCount, int totalItemCount) {}

    @Override
    protected void checkIfAllIsLoaded(int size) {
        allIsLoaded = true;
        Toast.makeText(getContext(), getResources().getText(R.string.all_scrobbles_are_loaded), Toast.LENGTH_SHORT).show();
    }
}