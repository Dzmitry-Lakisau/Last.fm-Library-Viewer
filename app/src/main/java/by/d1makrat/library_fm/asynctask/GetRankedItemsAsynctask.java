package by.d1makrat.library_fm.asynctask;

import android.os.AsyncTask;

import java.lang.reflect.Type;
import java.util.List;

import by.d1makrat.library_fm.model.RankedItem;
import by.d1makrat.library_fm.model.Scrobble;
//TODO refactor to operation example @GetSessionKeyAsyncTask
public abstract class GetRankedItemsAsynctask extends AsyncTask<String, Void, List<RankedItem>> {
    protected abstract List<RankedItem> doInBackground(String... params);
}