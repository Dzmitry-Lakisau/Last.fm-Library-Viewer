package by.d1makrat.library_fm.asynctask;

import android.os.AsyncTask;

import com.google.firebase.crash.FirebaseCrash;

import by.d1makrat.library_fm.model.TopArtists;
import by.d1makrat.library_fm.operation.IOperation;

public class GetTopArtistsAsyncTask extends AsyncTask<IOperation<TopArtists>, Void, TopArtists> {

    private Exception mException = null;
    private final GetTopArtistsCallback mGetTopArtistsCallback;

    public GetTopArtistsAsyncTask(GetTopArtistsCallback mGetTopArtistsCallback) {
        this.mGetTopArtistsCallback = mGetTopArtistsCallback;
    }

    @Override
    protected TopArtists doInBackground(IOperation<TopArtists>[] operations) {
        TopArtists result = null;

        try {
            result = operations[0].perform();
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrash.report(e);
            mException = e;
        }

        return result;
    }

    @Override
    protected void onPostExecute(TopArtists result) {
        if (mException != null)
            mGetTopArtistsCallback.onException(mException);
        else {
            mGetTopArtistsCallback.onLoadingSuccessful(result);
        }
    }
}
