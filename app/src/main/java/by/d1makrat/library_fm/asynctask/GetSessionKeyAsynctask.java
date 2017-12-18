package by.d1makrat.library_fm.asynctask;

import android.os.AsyncTask;

import java.net.URL;

import by.d1makrat.library_fm.APIException;
import by.d1makrat.library_fm.GetSessionKeyAsynctaskCallback;
import by.d1makrat.library_fm.NetworkRequester;
import by.d1makrat.library_fm.UrlConstructor;
import by.d1makrat.library_fm.json.JsonParser;

public class GetSessionKeyAsynctask extends AsyncTask<String, Void, String> {

    private GetSessionKeyAsynctaskCallback asynctaskCallback;
    private Exception mException = null;

    public GetSessionKeyAsynctask(GetSessionKeyAsynctaskCallback pAsynctaskCallback) {
        asynctaskCallback = pAsynctaskCallback;
    }

    @Override
    protected String doInBackground(String... params) {
        URL apiRequestUrl;
        String sessionKey = null;

        String username = params[0];
        String password = params[1];

        try {
            UrlConstructor urlConstructor = new UrlConstructor();
            apiRequestUrl = urlConstructor.constructGetSessionKeyApiRequestUrl(username, password);

            NetworkRequester networkRequester = new NetworkRequester();
            String response = networkRequester.request(apiRequestUrl, "POST");

            JsonParser jsonParser = new JsonParser();

            String errorOrNot = jsonParser.checkForApiErrors(response);
            if (!errorOrNot.equals("No error"))
                mException = new APIException(errorOrNot);
            else
                sessionKey = jsonParser.parseSessionkey(response);
        } catch (Exception e) {
            e.printStackTrace();
            mException = e;
        }

        return sessionKey;
    }

    @Override
    protected void onPostExecute(String result) {
        if (mException == null && result.length() == 32)
            asynctaskCallback.onSessionKeyGranted(result);
        else
            asynctaskCallback.onException(mException);
    }
}
