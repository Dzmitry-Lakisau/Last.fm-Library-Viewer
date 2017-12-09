package by.d1makrat.library_fm.ui.fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.asynctask.GetScrobblesOfTrackAsynctask;

public class ScrobblesOfTrackFragment extends ScrobblesListFragment {

    private static final String ARTIST_KEY = "artist";
    private static final java.lang.String TRACK_KEY = "track";
    private static String artist;
    private static String track;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        artist = getArguments().getString(ARTIST_KEY);
        track = getArguments().getString(TRACK_KEY);
        urlForBrowser = "https://www.last.fm/user/" + AppContext.getInstance().getUsername() + "/library/music/" + artist + "/_/" + track;
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
        mGetScrobblesAsynctask = new GetScrobblesOfTrackAsynctask(this);
        mGetScrobblesAsynctask.execute(asynctaskArgs);
    }
}