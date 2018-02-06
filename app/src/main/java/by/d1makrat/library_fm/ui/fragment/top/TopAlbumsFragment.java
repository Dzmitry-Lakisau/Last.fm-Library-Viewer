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
import by.d1makrat.library_fm.adapter.list.TopAlbumsAdapter;
import by.d1makrat.library_fm.asynctask.GetTopItemsAsyncTask;
import by.d1makrat.library_fm.model.TopAlbum;
import by.d1makrat.library_fm.operation.TopAlbumsOperation;
import by.d1makrat.library_fm.ui.CenteredToast;
import by.d1makrat.library_fm.ui.fragment.scrobble.ScrobblesOfAlbumFragment;
import by.d1makrat.library_fm.ui.fragment.scrobble.ScrobblesOfArtistFragment;

import static by.d1makrat.library_fm.Constants.ALBUM_KEY;
import static by.d1makrat.library_fm.Constants.ARTIST_KEY;
import static by.d1makrat.library_fm.Constants.SCROBBLES_OF_ALBUM_TAG;
import static by.d1makrat.library_fm.Constants.SCROBBLES_OF_ARTIST_TAG;

public class TopAlbumsFragment extends TopItemsFragment<TopAlbum> {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.top_albums);

        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(mPeriod.hashCode(), MENU_SCROBBLES_OF_ARTIST, 0, R.string.scrobbles_of_artist);
        menu.add(mPeriod.hashCode(), MENU_SCROBBLES_OF_ALBUM, 1, R.string.scrobbles_of_album);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getGroupId() == mPeriod.hashCode()) {
            switch (item.getItemId()) {
                case MENU_SCROBBLES_OF_ARTIST:
                    TopAlbum listItemPressed = mListAdapter.getSelectedItem();

                    Bundle bundle = new Bundle();
                    bundle.putString(ARTIST_KEY, listItemPressed.getArtistName());

                    Fragment fragment = new ScrobblesOfArtistFragment();
                    fragment.setArguments(bundle);

                    getParentFragment().getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.appear_from_right, R.anim.disappear_to_left,
                                    R.anim.appear_from_left, R.anim.disappear_to_right)
                            .replace(R.id.content_main, fragment, SCROBBLES_OF_ARTIST_TAG)
                            .addToBackStack(null)
                            .commit();
                    return true;
                case MENU_SCROBBLES_OF_ALBUM:
                    listItemPressed = mListAdapter.getSelectedItem();

                    bundle = new Bundle();
                    bundle.putString(ARTIST_KEY, listItemPressed.getArtistName());
                    bundle.putString(ALBUM_KEY, listItemPressed.getTitle());

                    fragment = new ScrobblesOfAlbumFragment();
                    fragment.setArguments(bundle);

                    getParentFragment().getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.appear_from_right, R.anim.disappear_to_left,
                                    R.anim.appear_from_left, R.anim.disappear_to_right)
                            .replace(R.id.content_main, fragment, SCROBBLES_OF_ALBUM_TAG)
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
        listHeadText = String.format(getString(R.string.total_albums), pItemsCount);
        listHeadTextView.setVisibility(View.VISIBLE);
        listHeadTextView.setText(listHeadText);
    }

    @Override
    protected void checkIfAllIsLoaded(int size) {
        if (size < AppContext.getInstance().getLimit()){
            allIsLoaded = true;
            CenteredToast.show(getContext(), R.string.all_albums_are_loaded, Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected TopAlbumsAdapter createAdapter() {
        return new TopAlbumsAdapter(getActivity().getLayoutInflater(), ContextCompat.getDrawable(getActivity(), R.drawable.img_vinyl));
    }

    @Override
    public void performOperation() {
        TopAlbumsOperation topAlbumsOperation = new TopAlbumsOperation(mPeriod, mPage);
        GetTopItemsAsyncTask<TopAlbum> getTopItemsAsyncTask = new GetTopItemsAsyncTask<>(this);
        getTopItemsAsyncTask.execute(topAlbumsOperation);
    }
}
