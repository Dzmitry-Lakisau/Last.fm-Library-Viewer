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
import by.d1makrat.library_fm.operation.ScrobblesOfTrackOperation;
import by.d1makrat.library_fm.ui.CenteredToast;

import static by.d1makrat.library_fm.Constants.ARTIST_KEY;
import static by.d1makrat.library_fm.Constants.TRACK_KEY;

public class ScrobblesOfTrackFragment extends ScrobblesFragment {

    private String artist;
    private String track;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            artist = getArguments().getString(ARTIST_KEY);
            track = getArguments().getString(TRACK_KEY);
        }
        mUrlForBrowser = AppContext.getInstance().getUser().getUrl() + "/library/music/" + artist + "/_/" + track;

        loadItems();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v ,menuInfo);

        menu.findItem(R.id.scrobbles_of_track).setVisible(false);
    }

    @Override
    public void performOperation() {
        ScrobblesOfTrackOperation scrobblesOfTrackOperation = new ScrobblesOfTrackOperation(artist, track, mFrom, mTo);
        GetItemsAsyncTask<Scrobble> getItemsAsyncTask = new GetItemsAsyncTask<>(this);
        getItemsAsyncTask.execute(scrobblesOfTrackOperation);
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
            actionBar.setSubtitle(track);
        }
    }
}
