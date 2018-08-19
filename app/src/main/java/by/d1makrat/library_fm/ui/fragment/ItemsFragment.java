package by.d1makrat.library_fm.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.adapter.list.ItemsAdapter;
import by.d1makrat.library_fm.ui.CenteredToast;
import by.d1makrat.library_fm.ui.fragment.scrobble.ScrobblesOfAlbumFragment;
import by.d1makrat.library_fm.ui.fragment.scrobble.ScrobblesOfArtistFragment;
import by.d1makrat.library_fm.ui.fragment.scrobble.ScrobblesOfTrackFragment;

import static by.d1makrat.library_fm.Constants.ALBUM_KEY;
import static by.d1makrat.library_fm.Constants.ARTIST_KEY;
import static by.d1makrat.library_fm.Constants.SCROBBLES_OF_ALBUM_TAG;
import static by.d1makrat.library_fm.Constants.SCROBBLES_OF_ARTIST_TAG;
import static by.d1makrat.library_fm.Constants.SCROBBLES_OF_TRACK_TAG;
import static by.d1makrat.library_fm.Constants.TRACK_KEY;

public abstract class ItemsFragment<T> extends Fragment{

    protected String mUrlForBrowser;

    protected AsyncTask mGetItemsAsynctask;
    protected ItemsAdapter<T> mListAdapter;
    private LinearLayoutManager mLayoutManager;
    protected int mPage = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        mListAdapter = createAdapter(getLayoutInflater());
    }

    protected void killTaskIfRunning(AsyncTask task) {
        if (task != null && task.getStatus() != AsyncTask.Status.FINISHED) {
            mPage--;
            task.cancel(true);
        }
    }

    protected void loadItems() {
        if (mPage == 1) {
            mListAdapter.addHeader();
        }
        else {
            mListAdapter.addFooter();
        }

        performOperation();
    }

    protected abstract ItemsAdapter<T> createAdapter(LayoutInflater pLayoutInflater);

    private final RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (mLayoutManager.findFirstVisibleItemPosition() == 0) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int visibleItemCount = mLayoutManager.getChildCount();
                    int totalItemCount = mLayoutManager.getItemCount();
                    int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();
                    if ((firstVisibleItemPosition + visibleItemCount) == totalItemCount && (totalItemCount > 0)) {
                        if (!mListAdapter.allIsLoaded && !mListAdapter.isLoading()) {
                            mPage++;
                            loadItems();
                        }
                    }
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount = mLayoutManager.getChildCount();
            int totalItemCount = mLayoutManager.getItemCount();
            int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();
            if ((firstVisibleItemPosition + visibleItemCount) >= totalItemCount && (totalItemCount > 0)) {
                if (!mListAdapter.allIsLoaded && !mListAdapter.isLoading()){
                    mPage++;
                    loadItems();
                }
            }
        }
    };

    protected abstract void performOperation();

    @Override
    public void onStop() {
        super.onStop();

        killTaskIfRunning(mGetItemsAsynctask);
    }

    protected abstract void checkIfAllIsLoaded(int size);

    public void onException(Exception pException) {//TODO ? add footer with retry behavior
        mListAdapter.removeAllHeadersAndFooters();

        mPage--;

        if (mListAdapter.isEmpty()) {
            mListAdapter.addErrorHeader();
        }

        CenteredToast.show(getContext(), pException.getMessage(), Toast.LENGTH_SHORT);
    }

    protected void setUpRecyclerView(View pRootView){

        RecyclerView mRecyclerView = pRootView.findViewById(R.id.rv);

        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mListAdapter);
        mRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        registerForContextMenu(mRecyclerView);
    }

    protected abstract void setUpActionBar(AppCompatActivity pActivity);

    protected void replaceFragment(@NonNull String pArtist, @Nullable String pTrack, @Nullable String pAlbum){
        Bundle bundle = new Bundle();
        Fragment fragment;
        String tag;

        if (pTrack == null && pAlbum == null){
            bundle.putString(ARTIST_KEY, pArtist);

            fragment = new ScrobblesOfArtistFragment();
            fragment.setArguments(bundle);

            tag = SCROBBLES_OF_ARTIST_TAG;
        }
        else if (pAlbum == null) {
            bundle.putString(ARTIST_KEY, pArtist);
            bundle.putString(TRACK_KEY, pTrack);

            fragment = new ScrobblesOfTrackFragment();
            fragment.setArguments(bundle);

            tag = SCROBBLES_OF_TRACK_TAG;
        }
        else {
            bundle.putString(ARTIST_KEY, pArtist);
            bundle.putString(ALBUM_KEY, pAlbum);

            fragment = new ScrobblesOfAlbumFragment();
            fragment.setArguments(bundle);

            tag = SCROBBLES_OF_ALBUM_TAG;
        }

        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.appear_from_right, R.anim.disappear_to_left,
                            R.anim.appear_from_left, R.anim.disappear_to_right)
                    .replace(R.id.content_main, fragment, tag)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
