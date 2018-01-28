package by.d1makrat.library_fm.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.adapter.list.ItemsAdapter;
import by.d1makrat.library_fm.ui.CenteredToast;

public abstract class ItemsFragment<T> extends Fragment{

    protected String mUrlForBrowser;

    protected boolean isLoading = false;
//    protected boolean isViewAlreadyCreated = false;
    protected boolean allIsLoaded = false;
    protected boolean isEmpty = false;

    protected AsyncTask mGetItemsAsynctask;
    protected ItemsAdapter<T> mListAdapter;
    protected LinearLayoutManager mLayoutManager;
    protected RecyclerView mRecyclerView;
    protected int mPage = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        mListAdapter = createAdapter();
    }

    public abstract View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        if (!isViewAlreadyCreated)
//            loadItems();
//    }

    protected abstract Fragment createFragment(String pTypeOfFragment, T t);

    protected void killTaskIfRunning(AsyncTask task) {
        if (task != null && task.getStatus() != AsyncTask.Status.FINISHED) {
            mPage--;
            task.cancel(true);
        }
    }

    public void loadItems() {

        isLoading = true;

        if (mPage == 1) {
            mListAdapter.addHeader();
        }
        else {
            mListAdapter.addFooter();
        }

        performOperation();
    }

    protected abstract ItemsAdapter<T> createAdapter();

    protected RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount = mLayoutManager.getChildCount();
            int totalItemCount = mLayoutManager.getItemCount();
            int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();
            if ((firstVisibleItemPosition + visibleItemCount) >= totalItemCount && (totalItemCount > 0)) {
                if (!allIsLoaded && !isLoading){
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

    protected void checkIfAllIsLoaded(int size){
        if (size < AppContext.getInstance().getLimit()){
            allIsLoaded = true;
            CenteredToast.show(getContext(), R.string.all_scrobbles_are_loaded, Toast.LENGTH_SHORT);
        }
    }
}