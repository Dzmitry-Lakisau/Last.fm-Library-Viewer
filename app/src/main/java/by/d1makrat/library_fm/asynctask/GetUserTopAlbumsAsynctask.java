package by.d1makrat.library_fm.asynctask;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import by.d1makrat.library_fm.APIException;
import by.d1makrat.library_fm.AsynctaskCallback;
import by.d1makrat.library_fm.HttpsClient;
import by.d1makrat.library_fm.UrlConstructor;
import by.d1makrat.library_fm.json.JsonParser;
import by.d1makrat.library_fm.model.RankedItem;

public class GetUserTopAlbumsAsynctask extends GetRankedItemsAsynctask {

    private Exception mException;
    private AsynctaskCallback asynctaskCallback;

    public GetUserTopAlbumsAsynctask(AsynctaskCallback pAsynctaskCallback) {
        asynctaskCallback = pAsynctaskCallback;
    }

    @Override
    protected List<RankedItem> doInBackground(String... params) {
        List<RankedItem> scrobbles = new ArrayList<>();

        try {
            UrlConstructor urlConstructor = new UrlConstructor();
            URL apiRequestUrl = urlConstructor.constructGetUserTopAlbumsApiRequestUrl(params[0], params[1]);

            HttpsClient httpsClient = new HttpsClient();
            String response = httpsClient.request(apiRequestUrl, "GET");

            JsonParser jsonParser = new JsonParser();

            String errorOrNot = jsonParser.checkForApiErrors(response);
            if (!errorOrNot.equals("No error"))
                mException = new APIException(errorOrNot);
            else
                scrobbles = jsonParser.parseUserTopAlbums(response);
        } catch (Exception e) {
            e.printStackTrace();
            mException = e;
        }

        return scrobbles;
    }

    @Override
    protected void onPostExecute(List<RankedItem> result) {
        if (mException != null)
            asynctaskCallback.onException(mException);
        else {
            asynctaskCallback.onLoadingRankedItemsSuccessful(result);
        }
    }
}