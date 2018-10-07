package by.d1makrat.library_fm.asynctask

import android.os.AsyncTask
import by.d1makrat.library_fm.model.TopItems
import by.d1makrat.library_fm.operation.IOperation
import com.google.firebase.crash.FirebaseCrash

class GetTopItemsAsyncTask<T>(private val mGetTopItemsCallback: GetTopItemsCallback<T>) : AsyncTask<IOperation<TopItems<T>>, Void, TopItems<T>?>() {
    private var mException: Exception? = null

    override fun doInBackground(operations: Array<IOperation<TopItems<T>>>): TopItems<T>? {
        var result: TopItems<T>? = null

        try {
            result = operations[0].perform()
        } catch (e: Exception) {
            e.printStackTrace()
            FirebaseCrash.report(e)
            mException = e
        }

        return result
    }

    override fun onPostExecute(result: TopItems<T>?) {
        if (mException != null)
            mGetTopItemsCallback.onException(mException!!)
        else {
            mGetTopItemsCallback.onLoadingSuccessful(result!!)
        }
    }
}
