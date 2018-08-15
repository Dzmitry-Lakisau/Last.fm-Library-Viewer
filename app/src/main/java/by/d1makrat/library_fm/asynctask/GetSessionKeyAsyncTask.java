package by.d1makrat.library_fm.asynctask;

import android.os.AsyncTask;

import com.crashlytics.android.Crashlytics;

import java.net.URL;

import by.d1makrat.library_fm.APIException;
import by.d1makrat.library_fm.https.HttpsClient;
import by.d1makrat.library_fm.utils.UrlConstructor;
import by.d1makrat.library_fm.https.RequestMethod;
import by.d1makrat.library_fm.json.JsonParser;

import static by.d1makrat.library_fm.Constants.API_NO_ERROR;

public class GetSessionKeyAsyncTask extends AsyncTask<String, Void, String> {

    private final GetSessionKeyCallback asynctaskCallback;
    private Exception mException = null;

    public GetSessionKeyAsyncTask(GetSessionKeyCallback pAsynctaskCallback) {
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

            HttpsClient httpsClient = new HttpsClient();
            String response = httpsClient.request(apiRequestUrl, RequestMethod.POST);

            JsonParser jsonParser = new JsonParser();

            String errorOrNot = jsonParser.checkForApiErrors(response);
            if (!errorOrNot.equals(API_NO_ERROR))
                throw new APIException(errorOrNot);
            else
                sessionKey = jsonParser.parseSessionkey(response);
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
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
