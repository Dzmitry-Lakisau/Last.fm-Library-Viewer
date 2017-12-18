package by.d1makrat.library_fm.ui.fragment;

import android.os.Bundle;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.asynctask.GetRecentScrobblesAsynctask;

public class RecentScrobblesFragment extends ScrobblesListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        urlForBrowser = AppContext.getInstance().getUser().getUrl() + "/library";
    }

    @Override
    public void loadItemsFromWeb() {
        String[] asynctaskArgs = {String.valueOf(mPage), mFrom, mTo};
        mGetScrobblesAsynctask = new GetRecentScrobblesAsynctask(this);
        mGetScrobblesAsynctask.execute(asynctaskArgs);
    }
}