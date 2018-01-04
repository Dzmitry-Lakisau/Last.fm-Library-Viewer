package by.d1makrat.library_fm.asynctask;

import android.os.AsyncTask;

import java.net.URL;

import by.d1makrat.library_fm.APIException;
import by.d1makrat.library_fm.GetUserInfoAsynctaskCallback;
import by.d1makrat.library_fm.HttpsClient;
import by.d1makrat.library_fm.UrlConstructor;
import by.d1makrat.library_fm.json.JsonParser;
import by.d1makrat.library_fm.model.User;

//TODO refactor to operation example @GetSessionKeyAsyncTask
public class GetUserInfoAsynctask extends AsyncTask<Void, Void, User> {

    private GetUserInfoAsynctaskCallback mAsynctaskCallback;
    private Exception mException = null;

    public GetUserInfoAsynctask(GetUserInfoAsynctaskCallback pGetUserInfoAsynctaskCallback) {
        mAsynctaskCallback = pGetUserInfoAsynctaskCallback;
    }

    @Override
    protected User doInBackground(Void... params) {

        User user = null;

        try {
            UrlConstructor urlConstructor = new UrlConstructor();
            URL apiRequestUrl = urlConstructor.constructGetUserInfoApiRequestUrl(null);

            //TODO move to userInfoOperation
            HttpsClient httpsClient = new HttpsClient();
            String response = httpsClient.request(apiRequestUrl, "GET");

            JsonParser jsonParser = new JsonParser();

            String errorOrNot = jsonParser.checkForApiErrors(response);
            if (!errorOrNot.equals("No error"))
                mException = new APIException(errorOrNot);
            else
                user = jsonParser.parseUserInfo(response);
        } catch (Exception e) {
            e.printStackTrace();
            mException = e;
        }

        return user;
    }

    @Override
    protected void onPostExecute(User result) {
        if (mException == null) {
            mAsynctaskCallback.onUserInfoReceived(result);
        }
        else
            mAsynctaskCallback.onException(mException);
    }
}
