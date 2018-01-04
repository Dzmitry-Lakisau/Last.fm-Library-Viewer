package by.d1makrat.library_fm.asynctask;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import by.d1makrat.library_fm.APIException;
import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.AsynctaskCallback;
import by.d1makrat.library_fm.HttpsClient;
import by.d1makrat.library_fm.UrlConstructor;
import by.d1makrat.library_fm.database.DatabaseWorker;
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
        List<RankedItem> topAlbums = new ArrayList<>();
        DatabaseWorker databaseWorker = new DatabaseWorker(AppContext.getInstance().getApplicationContext());

        try {
            databaseWorker.openDatabase();

            if (HttpsClient.isNetworkAvailable()) {
                UrlConstructor urlConstructor = new UrlConstructor();
                URL apiRequestUrl = urlConstructor.constructGetUserTopAlbumsApiRequestUrl(params[0], params[1]);

                HttpsClient httpsClient = new HttpsClient();
                String response = httpsClient.request(apiRequestUrl, "GET");

                JsonParser jsonParser = new JsonParser();

                String errorOrNot = jsonParser.checkForApiErrors(response);
                if (!errorOrNot.equals("No error"))
                    mException = new APIException(errorOrNot);
                else
                    topAlbums = jsonParser.parseUserTopAlbums(response);

                if (params[1].equals("1"))
                    databaseWorker.deleteTopAlbums(params[0]);

                databaseWorker.bulkInsertTopAlbums(topAlbums, params[0]);
            }
            else {
                topAlbums = databaseWorker.getTopAlbums(params[0], params[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mException = e;
        } finally {
            databaseWorker.closeDatabase();
        }

        return topAlbums;
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