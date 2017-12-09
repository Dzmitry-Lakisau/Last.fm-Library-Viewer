package by.d1makrat.library_fm.ui.fragment;

import by.d1makrat.library_fm.asynctask.GetRecentScrobblesAsynctask;

public class RecentScrobblesFragment extends ScrobblesListFragment {

    @Override
    public void loadItemsFromWeb() {
        String[] asynctaskArgs = {String.valueOf(mPage), mFrom, mTo};
        mGetScrobblesAsynctask = new GetRecentScrobblesAsynctask(this);
        mGetScrobblesAsynctask.execute(asynctaskArgs);
    }
}