package by.d1makrat.library_fm.ui.fragment.top;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.adapter.list.TopArtistsAdapter;
import by.d1makrat.library_fm.asynctask.GetTopItemsAsyncTask;
import by.d1makrat.library_fm.model.TopArtist;
import by.d1makrat.library_fm.operation.TopArtistsOperation;
import by.d1makrat.library_fm.operation.TopArtistsOperation;
import by.d1makrat.library_fm.ui.CenteredToast;
import by.d1makrat.library_fm.ui.fragment.scrobble.ScrobblesOfArtistFragment;

import static by.d1makrat.library_fm.Constants.ARTIST_KEY;
import static by.d1makrat.library_fm.Constants.SCROBBLES_OF_ARTIST_TAG;

public class TopArtistsFragment extends TopItemsFragment<TopArtist> {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.top_artists);

        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(0, MENU_SCROBBLES_OF_ARTIST, 0, R.string.scrobbles_of_artist);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getGroupId() == mPeriod.hashCode()) {
            switch (item.getItemId()) {
                case MENU_SCROBBLES_OF_ARTIST:
                    TopArtist listItemPressed = mListAdapter.getSelectedItem();

                    Bundle bundle = new Bundle();
                    bundle.putString(ARTIST_KEY, listItemPressed.getName());

                    Fragment fragment = new ScrobblesOfArtistFragment();
                    fragment.setArguments(bundle);

                    getParentFragment().getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.appear_from_right, R.anim.disappear_to_left,
                                    R.anim.appear_from_left, R.anim.disappear_to_right)
                            .replace(R.id.content_main, fragment, SCROBBLES_OF_ARTIST_TAG)
                            .addToBackStack(null)
                            .commit();
                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        }
        else return super.onContextItemSelected(item);
    }

    @Override
    public void setUpListHead(String pItemsCount) {
        listHeadText = String.format(getString(R.string.total_artists), pItemsCount);
        listHeadTextView.setVisibility(View.VISIBLE);
        listHeadTextView.setText(listHeadText);
    }

    @Override
    protected void checkIfAllIsLoaded(int size) {
        if (size < AppContext.getInstance().getLimit()){
            allIsLoaded = true;
            CenteredToast.show(getContext(), R.string.all_artists_are_loaded, Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected TopArtistsAdapter createAdapter() {
        return new TopArtistsAdapter(getActivity().getLayoutInflater(), ContextCompat.getDrawable(getActivity(), R.drawable.ic_person));
    }

    @Override
    public void performOperation() {
        TopArtistsOperation topArtistsOperation = new TopArtistsOperation(mPeriod, mPage);
        GetTopItemsAsyncTask<TopArtist> getTopItemsAsyncTask = new GetTopItemsAsyncTask<>(this);
        getTopItemsAsyncTask.execute(topArtistsOperation);
    }
}
