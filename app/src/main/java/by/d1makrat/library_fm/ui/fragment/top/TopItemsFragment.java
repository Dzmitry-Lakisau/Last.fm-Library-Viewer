package by.d1makrat.library_fm.ui.fragment.top;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.asynctask.GetTopItemsCallback;
import by.d1makrat.library_fm.operation.model.TopOperationResult;
import by.d1makrat.library_fm.ui.CenteredToast;
import by.d1makrat.library_fm.ui.fragment.ItemsFragment;

import static by.d1makrat.library_fm.Constants.PERIOD_KEY;

public abstract class TopItemsFragment<T> extends ItemsFragment<T> implements GetTopItemsCallback<T> {

    protected static final int MENU_SCROBBLES_OF_ARTIST = 0;
    protected static final int MENU_SCROBBLES_OF_TRACK = 1;
    protected static final int MENU_SCROBBLES_OF_ALBUM = 2;

    protected String mPeriod;
    protected TextView listHeadTextView;
    protected String listHeadText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPeriod = getArguments().getString(PERIOD_KEY);

        loadItems();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_top, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                if (!isLoading) {
                    allIsLoaded = false;

                    killTaskIfRunning(mGetItemsAsynctask);

                    mListAdapter.removeAll();

                    mPage = 1;
                    loadItems();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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
            listHeadTextView.setText(listHeadText);
        } else {
            listHeadTextView.setVisibility(View.VISIBLE);
            listHeadTextView.setText(listHeadText);
        }

        return rootView;
    }

//    @Override
//    protected Fragment createFragment(String pTypeOfFragment, T t) {
//        return null;
//    }

    @Override
    public void onLoadingSuccessful(TopOperationResult<T> result) {
        isLoading = false;
//        isViewAlreadyCreated = true;

        mListAdapter.removeAllHeadersAndFooters();

//        isEmpty = mListAdapter.getItemCount() == 0;

        List<T> items = result.getItems();
        String itemsCount = result.getCount();

        int size = items.size();
        if (size > 0) {
            mListAdapter.addAll(items);
            setUpListHead(itemsCount);
        }
        else if (mListAdapter.isEmpty()){
            mListAdapter.addEmptyHeader(getString(R.string.no_items));
        }

        checkIfAllIsLoaded(size);
    }

    public abstract void setUpListHead(String itemsCount);

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

        if (pException instanceof APIException){
            CenteredToast.show(getActivity(), pException.getMessage(), Toast.LENGTH_SHORT);
        }
    }
}
