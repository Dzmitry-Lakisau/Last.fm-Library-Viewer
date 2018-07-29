package by.d1makrat.library_fm.ui.fragment.top;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.adapter.list.TopTracksAdapter;
import by.d1makrat.library_fm.asynctask.GetTopItemsAsyncTask;
import by.d1makrat.library_fm.model.Track;
import by.d1makrat.library_fm.operation.TopTracksOperation;
import by.d1makrat.library_fm.ui.CenteredToast;

public class TopTracksFragment extends TopItemsFragment<Track> {

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(mPeriod.hashCode(), MENU_SCROBBLES_OF_ARTIST, 0, R.string.scrobbles_of_artist);
        menu.add(mPeriod.hashCode(), MENU_SCROBBLES_OF_TRACK, 1, R.string.scrobbles_of_track);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getGroupId() == mPeriod.hashCode()) {
            switch (item.getItemId()) {
                case MENU_SCROBBLES_OF_ARTIST:
                    replaceFragment(mListAdapter.getSelectedItem().getArtistName(), null, null);
                    return true;
                case MENU_SCROBBLES_OF_TRACK:
                    Track listItemPressed = mListAdapter.getSelectedItem();
                    replaceFragment(listItemPressed.getArtistName(), listItemPressed.getTitle(), null);
                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        }
        else return super.onContextItemSelected(item);
    }

    @Override
    protected void setUpListHead(String pItemsCount, int pVisibility) {
        listHeadTextView.setVisibility(pVisibility);
        if (pVisibility == View.VISIBLE){
            listHeadTextView.setText(getString(R.string.total_tracks, pItemsCount));
        }
    }

    @Override
    protected void checkIfAllIsLoaded(int size) {
        if (size < AppContext.getInstance().getLimit()){
            allIsLoaded = true;
            CenteredToast.show(getContext(), R.string.all_tracks_are_loaded, Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected TopTracksAdapter createAdapter(LayoutInflater pLayoutInflater) {
        return new TopTracksAdapter(pLayoutInflater, ContextCompat.getDrawable(AppContext.getInstance(), R.drawable.img_vinyl));
    }

    @Override
    public void performOperation() {
        TopTracksOperation topTracksOperation = new TopTracksOperation(mPeriod, mPage);
        GetTopItemsAsyncTask<Track> getItemsAsyncTask = new GetTopItemsAsyncTask<>(this);
        getItemsAsyncTask.execute(topTracksOperation);
    }

    @Override
    protected void setUpActionBar(AppCompatActivity pActivity) {
        ActionBar actionBar = pActivity.getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle(R.string.top_tracks);
        }
    }
}
