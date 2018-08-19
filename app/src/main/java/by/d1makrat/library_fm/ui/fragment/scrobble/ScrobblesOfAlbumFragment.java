package by.d1makrat.library_fm.ui.fragment.scrobble;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Toast;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.asynctask.GetItemsAsyncTask;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.model.Scrobble;
import by.d1makrat.library_fm.operation.ScrobblesOfAlbumOperation;
import by.d1makrat.library_fm.ui.CenteredToast;

import static by.d1makrat.library_fm.Constants.ALBUM_KEY;
import static by.d1makrat.library_fm.Constants.ARTIST_KEY;

public class ScrobblesOfAlbumFragment extends ScrobblesFragment {

    private String artist;
    private String album;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            artist = getArguments().getString(ARTIST_KEY);
            album = getArguments().getString(ALBUM_KEY);
        }
        mUrlForBrowser = AppContext.getInstance().getUser().getUrl() + "/library/music/" + artist + "/" + album;

        loadItems();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v ,menuInfo);

        menu.findItem(R.id.scrobbles_of_album).setVisible(false);
    }

    @Override
    public void performOperation() {
        ScrobblesOfAlbumOperation scrobblesOfAlbumOperation = new ScrobblesOfAlbumOperation(artist, album, mFrom, mTo);
        GetItemsAsyncTask<Scrobble> getItemsAsyncTask = new GetItemsAsyncTask<>(this);
        getItemsAsyncTask.execute(scrobblesOfAlbumOperation);
    }

    @Override
    protected void checkIfAllIsLoaded(int size) {
        mListAdapter.allIsLoaded = true;
        CenteredToast.show(getContext(), R.string.all_scrobbles_are_loaded, Toast.LENGTH_SHORT);
    }

    @Override
    protected void setUpActionBar(AppCompatActivity pActivity) {
        ActionBar actionBar = pActivity.getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle(artist);
            actionBar.setSubtitle(album);
        }
    }
}
