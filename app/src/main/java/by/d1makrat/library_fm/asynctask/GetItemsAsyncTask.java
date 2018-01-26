package by.d1makrat.library_fm.asynctask;

import android.os.AsyncTask;

import java.util.List;

import by.d1makrat.library_fm.operation.IOperation;

public class GetItemsAsyncTask<T> extends AsyncTask<IOperation<List<T>>, Void, List<T>> {

    private Exception mException = null;
    private GetItemsCallback<T> mGetItemsCallback;

    public GetItemsAsyncTask(GetItemsCallback<T> mGetItemsCallback) {
        this.mGetItemsCallback = mGetItemsCallback;
    }

    @Override
    protected List<T> doInBackground(IOperation<List<T>>[] operations) {
        List<T> items = null;

        try {
             items = operations[0].perform();
        } catch (Exception e) {
            e.printStackTrace();
            mException = e;
        }

        return items;
    }

    @Override
    protected void onPostExecute(List<T> items) {
        if (mException != null)
            mGetItemsCallback.onException(mException);
        else {
            mGetItemsCallback.onLoadingSuccessful(items);
        }
    }
}