package by.d1makrat.library_fm.asynctask;

import android.os.AsyncTask;

import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONObject;

import java.net.URL;

import by.d1makrat.library_fm.https.HttpsClient;
import by.d1makrat.library_fm.https.RequestMethod;

public class CheckNewVersionAsyncTask extends AsyncTask<Void, Void, Integer> {

    private static final String BACKEND_URL = "https://lastfmlibraryviewer.appspot.com/_ah/api/versionApi/v1/version";
    private static final String LATEST_VERSION_KEY = "latestVersion";

    private Exception mException = null;
    private final CheckNewVersionCallback mAsynctaskCallback;

    public CheckNewVersionAsyncTask(CheckNewVersionCallback pAsynctaskCallback) {
        mAsynctaskCallback = pAsynctaskCallback;
    }

    @Override
    protected Integer doInBackground(Void... voids) {

        Integer result = null;

        try {
            HttpsClient httpsClient = new HttpsClient();
            String response = httpsClient.request(new URL(BACKEND_URL), RequestMethod.GET);

            JSONObject jsonObject = new JSONObject(response);
            result = jsonObject.getInt(LATEST_VERSION_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrash.report(e);
            mException = e;
        }

        return result;
    }

    @Override
    protected void onPostExecute (Integer result) {
        if (mException == null) {
            mAsynctaskCallback.onSuccess(result);
        }
    }
}
