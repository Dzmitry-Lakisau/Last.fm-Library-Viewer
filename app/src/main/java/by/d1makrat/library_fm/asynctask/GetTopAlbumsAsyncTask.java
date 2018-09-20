package by.d1makrat.library_fm.asynctask;

import android.os.AsyncTask;

import com.google.firebase.crash.FirebaseCrash;

import by.d1makrat.library_fm.model.TopAlbums;
import by.d1makrat.library_fm.operation.IOperation;

public class GetTopAlbumsAsyncTask extends AsyncTask<IOperation<TopAlbums>, Void, TopAlbums> {

    private Exception mException = null;
    private final GetTopAlbumsCallback mGetTopAlbumsCallback;

    public GetTopAlbumsAsyncTask(GetTopAlbumsCallback mGetTopAlbumsCallback) {
        this.mGetTopAlbumsCallback = mGetTopAlbumsCallback;
    }

    @Override
    protected TopAlbums doInBackground(IOperation<TopAlbums>[] operations) {
        TopAlbums result = null;

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
    protected void onPostExecute(TopAlbums result) {
        if (mException != null)
            mGetTopAlbumsCallback.onException(mException);
        else {
            mGetTopAlbumsCallback.onLoadingSuccessful(result);
        }
    }
}
