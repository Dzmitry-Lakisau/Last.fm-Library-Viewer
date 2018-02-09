package by.d1makrat.library_fm.ui.fragment.scrobble;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

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
        }
    }
}
