package by.d1makrat.library_fm.ui.fragment.scrobble;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.asynctask.GetItemsAsyncTask;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.model.Scrobble;
import by.d1makrat.library_fm.operation.RecentScrobblesOperation;

public class RecentScrobblesFragment extends ScrobblesFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUrlForBrowser = AppContext.getInstance().getUser().getUrl() + "/library/";

        loadItems();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.scrobbles);

        return rootView;
    }

    @Override
    protected void performOperation() {
        RecentScrobblesOperation recentScrobblesOperation = new RecentScrobblesOperation(mPage, mFrom, mTo);
        GetItemsAsyncTask<Scrobble> getItemsAsyncTask = new GetItemsAsyncTask<>(this);
        getItemsAsyncTask.execute(recentScrobblesOperation);
    }
}