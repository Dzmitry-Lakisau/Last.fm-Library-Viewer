package by.d1makrat.library_fm.asynctask;

import android.os.AsyncTask;

import java.net.URL;

import by.d1makrat.library_fm.APIException;
import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.https.HttpsClient;
import by.d1makrat.library_fm.utils.UrlConstructor;
import by.d1makrat.library_fm.https.RequestMethod;
import by.d1makrat.library_fm.json.JsonParser;

import static by.d1makrat.library_fm.Constants.API_NO_ERROR;

public class SendScrobbleAsyncTask extends AsyncTask<String, Void, String> {

    private Exception mException = null;
    private final SendScrobbleCallback mAsynctaskCallback;

    public SendScrobbleAsyncTask(SendScrobbleCallback pAsynctaskCallback) {
        mAsynctaskCallback = pAsynctaskCallback;
    }

    @Override
    protected String doInBackground(String... params) {

        String result = null;

        try {
            UrlConstructor urlConstructor = new UrlConstructor();
            URL apiRequestUrl = urlConstructor.constructSendScrobbleApiRequestUrl(params[0], params[1], params[2], params[3], params[4], params[5]);

            HttpsClient httpsClient = new HttpsClient();
            String response = httpsClient.request(apiRequestUrl, RequestMethod.POST);

            JsonParser jsonParser = new JsonParser();

            String errorOrNot = jsonParser.checkForApiErrors(response);
            if (!errorOrNot.equals(API_NO_ERROR))
                throw new APIException(errorOrNot);
            else {
                result = jsonParser.parseSendScrobbleResult(response);
                if (!result.equals(AppContext.getInstance().getString(R.string.manual_fragment_scrobble_accepted))) {
                    throw new APIException(result);
                }
            }
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
