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
import by.d1makrat.library_fm.adapter.list.TopArtistsAdapter;
import by.d1makrat.library_fm.asynctask.GetTopItemsAsyncTask;
import by.d1makrat.library_fm.model.TopArtist;
import by.d1makrat.library_fm.operation.TopArtistsOperation;
import by.d1makrat.library_fm.ui.CenteredToast;

public class TopArtistsFragment extends TopItemsFragment<TopArtist> {

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(mPeriod.hashCode(), MENU_SCROBBLES_OF_ARTIST, 0, R.string.scrobbles_of_artist);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getGroupId() == mPeriod.hashCode()) {
            switch (item.getItemId()) {
                case MENU_SCROBBLES_OF_ARTIST:
                    replaceFragment(mListAdapter.getSelectedItem().getName(), null, null);
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
        listHeadTextView.setText(getString(R.string.total_artists, pItemsCount));
    }

    @Override
    protected void checkIfAllIsLoaded(int size) {
        if (size < AppContext.getInstance().getLimit()){
            allIsLoaded = true;
            CenteredToast.show(getContext(), R.string.all_artists_are_loaded, Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected TopArtistsAdapter createAdapter(LayoutInflater pLayoutInflater) {
        return new TopArtistsAdapter(pLayoutInflater, ContextCompat.getDrawable(AppContext.getInstance(), R.drawable.ic_person_black_24dp_large));
    }

    @Override
    public void performOperation() {
        TopArtistsOperation topArtistsOperation = new TopArtistsOperation(mPeriod, mPage);
        GetTopItemsAsyncTask<TopArtist> getTopItemsAsyncTask = new GetTopItemsAsyncTask<>(this);
        getTopItemsAsyncTask.execute(topArtistsOperation);
    }

    @Override
    protected void setUpActionBar(AppCompatActivity pActivity) {
        ActionBar actionBar = pActivity.getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle(R.string.top_artists);
        }
    }
}
