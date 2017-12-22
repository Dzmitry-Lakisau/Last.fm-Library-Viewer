package by.d1makrat.library_fm.asynctask;

import android.os.AsyncTask;

import java.net.URL;

import by.d1makrat.library_fm.APIException;
import by.d1makrat.library_fm.NetworkRequester;
import by.d1makrat.library_fm.SendScrobbleAsynctaskCallback;
import by.d1makrat.library_fm.UrlConstructor;
import by.d1makrat.library_fm.json.JsonParser;

public class SendScrobbleTask extends AsyncTask<String, Void, String> {

    private Exception mException = null;
    private SendScrobbleAsynctaskCallback mAsynctaskCallback;

    public SendScrobbleTask(SendScrobbleAsynctaskCallback pAsynctaskCallback) {
        mAsynctaskCallback = pAsynctaskCallback;
    }

    @Override
    protected String doInBackground(String... params) {

        String result = null;

        try {
            UrlConstructor urlConstructor = new UrlConstructor();
            URL apiRequestUrl = urlConstructor.constructSendScrobbleApiRequestUrl(params[0], params[1], params[2], params[3], params[4], params[5]);

            NetworkRequester networkRequester = new NetworkRequester();
            String response = networkRequester.request(apiRequestUrl, "POST");

            JsonParser jsonParser = new JsonParser();

            String errorOrNot = jsonParser.checkForApiErrors(response);
            if (!errorOrNot.equals("No error"))
                mException = new APIException(errorOrNot);
            else
                result = jsonParser.parseSendScrobbleResult(response);
        } catch (Exception e) {
            e.printStackTrace();
            mException = e;
        }

        return result;
    }

    @Override
    protected void onPostExecute (String result) {
        if (mException != null)
            mAsynctaskCallback.onException(mException);
        else
            mAsynctaskCallback.onSendScrobbleResult(result);
    }
}