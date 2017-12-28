package by.d1makrat.library_fm.ui.fragment;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.Toast;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.adapter.list.ScrobblesListAdapter;
import by.d1makrat.library_fm.asynctask.GetRecentScrobblesAsynctask;

public class RecentScrobblesFragment extends ScrobblesListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        urlForBrowser = AppContext.getInstance().getUser().getUrl() + "/library/";
    }

    @Override
    protected BaseAdapter createAdapter(){

        Drawable drawable;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = getResources().getDrawable(R.drawable.default_albumart);
        } else
            drawable = getResources().getDrawable(R.drawable.default_albumart, null);

        return new ScrobblesListAdapter(getActivity().getLayoutInflater(), drawable, mScrobbles);
    }

    @Override
    protected void loadItemsFromWeb() {
        String[] asynctaskArgs = {String.valueOf(mPage), mFrom, mTo};
        mGetItemsAsynctask = new GetRecentScrobblesAsynctask(this);
        mGetItemsAsynctask.execute(asynctaskArgs);
    }

    @Override
    protected void onAllIsLoaded(int pSize) {
        if (pSize < AppContext.getInstance().getLimit()) {
            allIsLoaded = true;
            Toast.makeText(getContext(), getResources().getText(R.string.all_scrobbles_are_loaded), Toast.LENGTH_SHORT).show();
        }
    }
}