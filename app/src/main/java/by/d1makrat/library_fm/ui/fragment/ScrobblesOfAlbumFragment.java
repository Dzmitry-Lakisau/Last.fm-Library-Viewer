package by.d1makrat.library_fm.ui.fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.asynctask.GetScrobblesOfAlbumAsynctask;

public class ScrobblesOfAlbumFragment extends ScrobblesListFragment {

    private static final String ARTIST_BUNDLE_KEY = "artist";
    private static final java.lang.String ALBUM_BUNDLE_KEY = "album";
    private static String artist;
    private static String album;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        artist = getArguments().getString(ARTIST_BUNDLE_KEY);
        album = getArguments().getString(ALBUM_BUNDLE_KEY);
        urlForBrowser = "https://www.last.fm/user/" + AppContext.getInstance().getUsername() + "/library/music/" + artist + "/" + album;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(artist);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(album);

        return rootView;
    }
//TODO check behaviour for limit>200

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(contextMenu, v ,menuInfo);

        contextMenu.getItem(2).setVisible(false);
    }

    @Override
    public void loadItemsFromWeb() {
        String[] asynctaskArgs = {artist, album, mFrom, mTo};
        mGetScrobblesAsynctask = new GetScrobblesOfAlbumAsynctask(this);
        mGetScrobblesAsynctask.execute(asynctaskArgs);
    }

    @Override
    public void onScroll(AbsListView l, int firstVisibleItem, int visibleItemCount, int totalItemCount) {}
}