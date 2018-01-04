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
import by.d1makrat.library_fm.model.Scrobble;
//TODO refactor to operation example @GetSessionKeyAsyncTask
public class GetScrobblesOfArtistAsynctask extends GetScrobblesAsynctask{

    //TODO refactor to Generic async task
    private Exception mException = null;
    private AsynctaskCallback mAsynctaskCallback;

    public GetScrobblesOfArtistAsynctask(AsynctaskCallback pAsynctaskCallback) {
        mAsynctaskCallback = pAsynctaskCallback;
    }

    @Override
    protected List<Scrobble> doInBackground(String... params) {

        List<Scrobble> scrobbles = new ArrayList<Scrobble>();
        URL apiRequestUrl;
        DatabaseWorker databaseWorker = new DatabaseWorker(AppContext.getInstance().getApplicationContext());

        try {
            databaseWorker.openDatabase();

            if (HttpsClient.isNetworkAvailable()) {
                UrlConstructor urlConstructor = new UrlConstructor();
                apiRequestUrl = urlConstructor.constructScrobblesByArtistApiRequestUrl(params[0], params[1], params[2], params[3]);

                HttpsClient httpsClient = new HttpsClient();
                String response = httpsClient.request(apiRequestUrl, "GET");

                JsonParser jsonParser = new JsonParser();

                String errorOrNot = jsonParser.checkForApiErrors(response);
                if (!errorOrNot.equals("No error"))
                    mException = new APIException(errorOrNot);
                else
                    scrobbles = jsonParser.parseScrobbles(response);
            }
            else {
                scrobbles = databaseWorker.getScrobblesOfArtist(params[0], params[1], params[2], params[3]);
            }
        } catch (Exception e) {
            //TODO read about DRY
            e.printStackTrace();
            mException = e;
        } finally {
            databaseWorker.closeDatabase();
        }

        return scrobbles;
    }

    @Override
    protected void onPostExecute(List<Scrobble> scrobbles) {
        if (mException != null)
            mAsynctaskCallback.onException(mException);
        else
            mAsynctaskCallback.onLoadingScrobblesSuccessful(scrobbles);
    }
}