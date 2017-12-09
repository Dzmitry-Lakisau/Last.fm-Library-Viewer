package by.d1makrat.library_fm.ui.fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.asynctask.GetScrobblesOfArtistAsynctask;

public class ScrobblesOfArtistFragment extends ScrobblesListFragment {

    private static final String ARTIST_KEY = "artist";
    private static String artist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        artist = getArguments().getString(ARTIST_KEY);
        urlForBrowser = "https://www.last.fm/user/" + AppContext.getInstance().getUsername() + "/library/music/" + artist;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(artist);

        return rootView;
    }
//TODO check behaviour for limit>200

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(contextMenu, v ,menuInfo);

        contextMenu.getItem(0).setVisible(false);
    }

    @Override
    public void loadItemsFromWeb() {
        String[] asynctaskArgs = {artist, String.valueOf(mPage), mFrom, mTo};
        mGetScrobblesAsynctask = new GetScrobblesOfArtistAsynctask(this);
        mGetScrobblesAsynctask.execute(asynctaskArgs);
    }
}