package by.d1makrat.library_fm.asynctask;

import android.os.AsyncTask;

import by.d1makrat.library_fm.operation.IOperation;
import by.d1makrat.library_fm.operation.model.TopOperationResult;

public class GetTopItemsAsyncTask<T> extends AsyncTask<IOperation<TopOperationResult<T>>, Void, TopOperationResult<T>> {

    private Exception mException = null;
    private final GetTopItemsCallback<T> mGetTopItemsCallback;

    public GetTopItemsAsyncTask(GetTopItemsCallback<T> mGetTopItemsCallback) {
        this.mGetTopItemsCallback = mGetTopItemsCallback;
    }

    @Override
    protected TopOperationResult<T> doInBackground(IOperation<TopOperationResult<T>>[] operations) {
        TopOperationResult<T> result = null;

        try {
            result = operations[0].perform();
        } catch (Exception e) {
            e.printStackTrace();
            mException = e;
        }

        return result;
    }

    @Override
    protected void onPostExecute(TopOperationResult<T> result) {
        if (mException != null)
            mGetTopItemsCallback.onException(mException);
        else {
            mGetTopItemsCallback.onLoadingSuccessful(result);
        }
    }
}
