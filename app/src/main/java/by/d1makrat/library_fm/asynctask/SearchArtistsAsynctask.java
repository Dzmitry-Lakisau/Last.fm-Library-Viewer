package by.d1makrat.library_fm.asynctask;

import android.os.AsyncTask;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import by.d1makrat.library_fm.APIException;
import by.d1makrat.library_fm.HttpsClient;
import by.d1makrat.library_fm.SearchArtistsAsynctaskCallback;
import by.d1makrat.library_fm.UrlConstructor;
import by.d1makrat.library_fm.json.JsonParser;
import by.d1makrat.library_fm.model.Artist;

//TODO refactor to operation example @GetSessionKeyAsyncTask
public class SearchArtistsAsynctask extends AsyncTask<String, Void, List<Artist>> {

    private Exception mException;
    private SearchArtistsAsynctaskCallback asynctaskCallback;

    public SearchArtistsAsynctask(SearchArtistsAsynctaskCallback pAsynctaskCallback) {
        asynctaskCallback = pAsynctaskCallback;
    }

    @Override
    protected List<Artist> doInBackground(String... params) {
        List<Artist> scrobbles = new ArrayList<>();

        try {
            UrlConstructor urlConstructor = new UrlConstructor();
            URL apiRequestUrl = urlConstructor.constructSearchArtistsApiRequestUrl(params[0], params[1]);

            HttpsClient httpsClient = new HttpsClient();
            String response = httpsClient.request(apiRequestUrl, "GET");

            JsonParser jsonParser = new JsonParser();

            String errorOrNot = jsonParser.checkForApiErrors(response);
            if (!errorOrNot.equals("No error"))
                mException = new APIException(errorOrNot);
            else
                scrobbles = jsonParser.parseSearchArtistResults(response);
        } catch (Exception e) {
            e.printStackTrace();
            mException = e;
        }

        return scrobbles;
    }

    @Override
    protected void onPostExecute(List<Artist> result) {
        if (mException != null)
            asynctaskCallback.onException(mException);
        else {
            asynctaskCallback.onLoadingSearchArtistsResultsSuccessful(result);
        }
    }
}