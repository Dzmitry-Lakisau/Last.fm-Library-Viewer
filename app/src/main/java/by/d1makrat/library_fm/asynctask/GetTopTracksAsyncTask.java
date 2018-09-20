package by.d1makrat.library_fm.asynctask;

import android.os.AsyncTask;

import com.google.firebase.crash.FirebaseCrash;

import by.d1makrat.library_fm.model.TopTracks;
import by.d1makrat.library_fm.operation.IOperation;

public class GetTopTracksAsyncTask extends AsyncTask<IOperation<TopTracks>, Void, TopTracks> {

    private Exception mException = null;
    private final GetTopTracksCallback mGetTopTracksCallback;

    public GetTopTracksAsyncTask(GetTopTracksCallback mGetTopTracksCallback) {
        this.mGetTopTracksCallback = mGetTopTracksCallback;
    }

    @Override
    protected TopTracks doInBackground(IOperation<TopTracks>[] operations) {
        TopTracks result = null;

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
    protected void onPostExecute(TopTracks result) {
        if (mException != null)
            mGetTopTracksCallback.onException(mException);
        else {
            mGetTopTracksCallback.onLoadingSuccessful(result);
        }
    }
}
