package by.d1makrat.library_fm.ui.fragment.top;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.asynctask.GetTopItemsCallback;
import by.d1makrat.library_fm.operation.model.TopOperationResult;
import by.d1makrat.library_fm.ui.fragment.ItemsFragment;

import static by.d1makrat.library_fm.Constants.PERIOD_KEY;

public abstract class TopItemsFragment<T> extends ItemsFragment<T> implements GetTopItemsCallback<T> {

    protected static final int MENU_SCROBBLES_OF_ARTIST = 0;
    protected static final int MENU_SCROBBLES_OF_TRACK = 1;
    protected static final int MENU_SCROBBLES_OF_ALBUM = 2;

    protected String mPeriod;
    protected TextView listHeadTextView;
    private String mTotalItemCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) mPeriod = getArguments().getString(PERIOD_KEY);

        loadItems();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                if (!isLoading) {
                    allIsLoaded = false;

                    killTaskIfRunning(mGetItemsAsynctask);

                    mListAdapter.removeAll();
                    setUpListHead(mTotalItemCount, View.GONE);

                    mPage = 1;
                    loadItems();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_with_head, container, false);

        setUpRecyclerView(rootView);
        setUpActionBar((AppCompatActivity) getActivity());

        listHeadTextView = rootView.findViewById(R.id.list_head);

        if (mListAdapter.isEmpty()) {
            setUpListHead(mTotalItemCount, View.GONE);
        } else {
            setUpListHead(mTotalItemCount, View.VISIBLE);
        }

        return rootView;
    }

    @Override
    public void onLoadingSuccessful(TopOperationResult<T> result) {
        isLoading = false;

        mListAdapter.removeAllHeadersAndFooters();

        List<T> items = result.getItems();
        mTotalItemCount = result.getCount();

        int size = items.size();
        if (size > 0) {
            mListAdapter.addAll(items);
            setUpListHead(mTotalItemCount, View.VISIBLE);
        }
        else if (mListAdapter.isEmpty()){
            mListAdapter.addEmptyHeader(getString(R.string.no_items));
        }

        checkIfAllIsLoaded(size);
    }

    protected abstract void setUpListHead(String pItemsCount, int pVisibility);
}
