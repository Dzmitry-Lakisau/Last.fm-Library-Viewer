package by.d1makrat.library_fm.ui.fragment.scrobble;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.asynctask.GetItemsAsyncTask;
import by.d1makrat.library_fm.model.Scrobble;
import by.d1makrat.library_fm.operation.RecentScrobblesOperation;

import static by.d1makrat.library_fm.Constants.DATE_LONG_DEFAUT_VALUE;
import static by.d1makrat.library_fm.Constants.FILTER_DIALOG_FROM_BUNDLE_KEY;
import static by.d1makrat.library_fm.Constants.FILTER_DIALOG_TO_BUNDLE_KEY;

public class RecentScrobblesFragment extends ScrobblesFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUrlForBrowser = AppContext.getInstance().getUser().getUrl() + "/library/";

        if (getArguments() != null) {
            mFrom = getArguments().getLong(FILTER_DIALOG_FROM_BUNDLE_KEY, DATE_LONG_DEFAUT_VALUE);
            mTo = getArguments().getLong(FILTER_DIALOG_TO_BUNDLE_KEY, DATE_LONG_DEFAUT_VALUE);
        }

        loadItems();
    }

    @Override
    protected void performOperation() {
        RecentScrobblesOperation recentScrobblesOperation = new RecentScrobblesOperation(mPage, mFrom, mTo);
        GetItemsAsyncTask<Scrobble> getItemsAsyncTask = new GetItemsAsyncTask<>(this);
        getItemsAsyncTask.execute(recentScrobblesOperation);
    }

    @Override
    protected void setUpActionBar(AppCompatActivity pActivity) {
        ActionBar actionBar = pActivity.getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle(R.string.scrobbles);
            actionBar.setSubtitle(null);
        }
    }
}
