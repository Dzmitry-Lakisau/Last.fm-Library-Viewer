package by.d1makrat.library_fm.ui.fragment.scrobble;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.View;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.asynctask.GetItemsAsyncTask;
import by.d1makrat.library_fm.model.Scrobble;
import by.d1makrat.library_fm.operation.ScrobblesOfArtistOperation;

import static by.d1makrat.library_fm.Constants.ARTIST_KEY;

public class ScrobblesOfArtistFragment extends ScrobblesFragment {

    private String artist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            artist = getArguments().getString(ARTIST_KEY);
        }
        mUrlForBrowser = AppContext.getInstance().getUser().getUrl() + "/library/music/" + artist;

        loadItems();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v ,menuInfo);

        menu.findItem(R.id.scrobbles_of_artist).setVisible(false);
    }

    @Override
    public void performOperation() {
        ScrobblesOfArtistOperation scrobblesOfArtistOperation = new ScrobblesOfArtistOperation(artist, mPage, mFrom, mTo);
        GetItemsAsyncTask<Scrobble> getItemsAsyncTask = new GetItemsAsyncTask<>(this);
        getItemsAsyncTask.execute(scrobblesOfArtistOperation);
    }

    @Override
    protected void setUpActionBar(AppCompatActivity pActivity) {
        ActionBar actionBar = pActivity.getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle(artist);
        }
    }
}
