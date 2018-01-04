package by.d1makrat.library_fm.asynctask;

import android.os.AsyncTask;

import java.util.List;

import by.d1makrat.library_fm.model.Scrobble;
//TODO refactor to operation example @GetSessionKeyAsyncTask
public abstract class GetScrobblesAsynctask extends AsyncTask<String, Void, List<Scrobble>>{
    protected abstract List<Scrobble> doInBackground(String... params);
}