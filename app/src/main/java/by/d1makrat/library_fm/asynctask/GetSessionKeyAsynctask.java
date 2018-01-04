package by.d1makrat.library_fm.asynctask;

import android.os.AsyncTask;

import java.net.URL;

import by.d1makrat.library_fm.APIException;
import by.d1makrat.library_fm.GetSessionKeyAsynctaskCallback;
import by.d1makrat.library_fm.HttpsClient;
import by.d1makrat.library_fm.UrlConstructor;
import by.d1makrat.library_fm.json.JsonParser;
import by.d1makrat.library_fm.operation.SessionKeyOperation;

public class GetSessionKeyAsynctask extends AsyncTask<String, Void, String> {

    private GetSessionKeyAsynctaskCallback asynctaskCallback;
    private Exception mException = null;

    public GetSessionKeyAsynctask(GetSessionKeyAsynctaskCallback pAsynctaskCallback) {
        asynctaskCallback = pAsynctaskCallback;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            return new SessionKeyOperation(params[0], params[1]).perform();
        } catch (Exception pException) {
            mException = pException;
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (mException == null && result.length() == 32)
            asynctaskCallback.onSessionKeyGranted(result);
        else
            asynctaskCallback.onException(mException);
    }
}
