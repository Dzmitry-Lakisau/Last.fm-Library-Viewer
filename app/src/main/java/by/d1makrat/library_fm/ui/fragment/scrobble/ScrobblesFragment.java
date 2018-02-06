package by.d1makrat.library_fm.ui.fragment.scrobble;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import by.d1makrat.library_fm.APIException;
import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.adapter.list.ScrobblesAdapter;
import by.d1makrat.library_fm.asynctask.GetItemsCallback;
import by.d1makrat.library_fm.model.Scrobble;
import by.d1makrat.library_fm.ui.CenteredToast;
import by.d1makrat.library_fm.ui.fragment.ItemsFragment;
import by.d1makrat.library_fm.ui.fragment.dialog.FilterDialogFragment;
import by.d1makrat.library_fm.utils.DateUtils;

import static by.d1makrat.library_fm.Constants.ALBUM_KEY;
import static by.d1makrat.library_fm.Constants.ARTIST_KEY;
import static by.d1makrat.library_fm.Constants.DATE_LONG_DEFAUT_VALUE;
import static by.d1makrat.library_fm.Constants.FILTER_DIALOG_FROM_BUNDLE_KEY;
import static by.d1makrat.library_fm.Constants.FILTER_DIALOG_TO_BUNDLE_KEY;
import static by.d1makrat.library_fm.Constants.SCROBBLES_OF_ALBUM_TAG;
import static by.d1makrat.library_fm.Constants.SCROBBLES_OF_ARTIST_TAG;
import static by.d1makrat.library_fm.Constants.SCROBBLES_OF_TRACK_TAG;
import static by.d1makrat.library_fm.Constants.TRACK_KEY;

public abstract class ScrobblesFragment extends ItemsFragment<Scrobble> implements FilterDialogFragment.FilterDialogListener, GetItemsCallback<Scrobble> {

    private static final String FILTER_DIALOG_KEY = "FilterDialogFragment";

    private TextView listHeadTextView;

    protected Long mFrom = DATE_LONG_DEFAUT_VALUE;
    protected Long mTo = DATE_LONG_DEFAUT_VALUE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.list_with_head, container, false);

        mRecyclerView = rootView.findViewById(R.id.rv);

        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mListAdapter);
        mRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        registerForContextMenu(mRecyclerView);

        listHeadTextView = rootView.findViewById(R.id.list_head);

        if (mListAdapter.isEmpty()) {
            listHeadTextView.setVisibility(View.GONE);
        } else {
            listHeadTextView.setVisibility(View.VISIBLE);
            listHeadTextView.setText(String.format(getString(R.string.recent_scrobbles_count), mListAdapter.getItemCount()));
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_scrobbles, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                    if (!isLoading) {
                        allIsLoaded = false;

                        listHeadTextView.setVisibility(View.GONE);

                        killTaskIfRunning(mGetItemsAsynctask);

                        mListAdapter.removeAll();

                        mPage = 1;
                        loadItems();
                    }
                return true;
            case R.id.action_filter:
                if (!isLoading) {
                    FilterDialogFragment dialogFragment = new FilterDialogFragment();
                    Bundle args = new Bundle();
                    args.putLong(FILTER_DIALOG_FROM_BUNDLE_KEY, mFrom);
                    args.putLong(FILTER_DIALOG_TO_BUNDLE_KEY, mTo);
                    dialogFragment.setArguments(args);
                    dialogFragment.setTargetFragment(this, 0);
                    dialogFragment.show(getFragmentManager(), FILTER_DIALOG_KEY);
                }
                return true;
            case R.id.open_in_browser:
                Intent intent;

                if (mFrom != null && mTo != null) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(DateUtils.getUrlFromTimestamps(mUrlForBrowser, mFrom, mTo)));
                }
                else {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrlForBrowser));
                }

                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.menu_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem pMenuItem) {

        Scrobble listItemPressed = mListAdapter.getSelectedItem();

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.appear_from_right, R.anim.disappear_to_left,
                R.anim.appear_from_left, R.anim.disappear_to_right);
        Fragment fragment;

        switch (pMenuItem.getItemId()) {
            case R.id.scrobbles_of_artist:
                fragment = createFragment(SCROBBLES_OF_ARTIST_TAG, listItemPressed);
                fragmentTransaction.replace(R.id.content_main, fragment, SCROBBLES_OF_ARTIST_TAG);
                break;
            case R.id.scrobbles_of_track:
                fragment = createFragment(SCROBBLES_OF_TRACK_TAG, listItemPressed);
                fragmentTransaction.replace(R.id.content_main, fragment, SCROBBLES_OF_TRACK_TAG);
                break;
            case R.id.scrobbles_of_album:
                fragment = createFragment(SCROBBLES_OF_ALBUM_TAG, listItemPressed);
                fragmentTransaction.replace(R.id.content_main, fragment, SCROBBLES_OF_ALBUM_TAG);
                break;
            default:
                return super.onContextItemSelected(pMenuItem);
        }
        fragmentTransaction.addToBackStack(null).commit();

        return true;
    }

    protected Fragment createFragment(String pTypeOfFragment, Scrobble scrobble){

        Fragment fragment = new Fragment();
        Bundle bundle = new Bundle();

        String artist = scrobble.getArtist();

        switch (pTypeOfFragment) {
            case SCROBBLES_OF_ARTIST_TAG:
                fragment = new ScrobblesOfArtistFragment();
                break;
            case SCROBBLES_OF_TRACK_TAG:
                fragment = new ScrobblesOfTrackFragment();
                bundle.putString(TRACK_KEY, scrobble.getTrackTitle());
                break;
            case SCROBBLES_OF_ALBUM_TAG:
                fragment = new ScrobblesOfAlbumFragment();
                bundle.putString(ALBUM_KEY, scrobble.getAlbum());
                break;
        }
        bundle.putString(ARTIST_KEY, artist);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onFinishFilterDialog(Long pFrom, Long pTo) {
        listHeadTextView.setVisibility(View.GONE);
        allIsLoaded = false;
        mFrom = pFrom;
        mTo = pTo;

        mListAdapter.removeAll();

        mPage = 1;

        killTaskIfRunning(mGetItemsAsynctask);
        loadItems();
    }

    @Override
    protected ScrobblesAdapter createAdapter(){
        return new ScrobblesAdapter(getActivity().getLayoutInflater(), ContextCompat.getDrawable(getActivity(), R.drawable.img_vinyl));
    }

    protected abstract void performOperation();

    @Override
    public void onStop() {
        super.onStop();

        killTaskIfRunning(mGetItemsAsynctask);
    }

    @Override
    public void onLoadingSuccessful(List<Scrobble> items) {
        isLoading = false;
//        isViewAlreadyCreated = true;

        mListAdapter.removeAllHeadersAndFooters();

//        isEmpty = mListAdapter.getItemCount() == 0;

        int size = items.size();
        if (size > 0) {
            mListAdapter.addAll(items);
            listHeadTextView.setVisibility(View.VISIBLE);
            listHeadTextView.setText(String.format(getString(R.string.recent_scrobbles_count), mListAdapter.getItemCount()));
        }
        else if (mListAdapter.isEmpty()){
            mListAdapter.addEmptyHeader(DateUtils.getMessageFromTimestamps(mFrom, mTo));
        }

        checkIfAllIsLoaded(size);
    }

    protected void checkIfAllIsLoaded(int size){
        if (size < AppContext.getInstance().getLimit()){
            allIsLoaded = true;
            CenteredToast.show(getContext(), R.string.all_scrobbles_are_loaded, Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onException(Exception pException) {
        isLoading = false;
//        isViewAlreadyCreated = true;

        mListAdapter.removeAllHeadersAndFooters();

        mPage--;

//        isEmpty = mListAdapter.getItemCount() == 0;
        if (mListAdapter.isEmpty()) {
            mListAdapter.addErrorHeader();
        }
        //TODO ? add footer with retry behavior
        if (pException instanceof APIException){
            CenteredToast.show(getContext(), pException.getMessage(), Toast.LENGTH_SHORT);
        }
    }
}
