package by.d1makrat.library_fm.ui.fragment;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.adapter.list.ScrobblesListAdapter;
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
        urlForBrowser = AppContext.getInstance().getUser().getUrl() + "/library/music/" + artist + "/" + album;
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
        mGetItemsAsynctask = new GetScrobblesOfAlbumAsynctask(this);
        mGetItemsAsynctask.execute(asynctaskArgs);
    }

    @Override
    public void onScroll(AbsListView l, int firstVisibleItem, int visibleItemCount, int totalItemCount) {}
}