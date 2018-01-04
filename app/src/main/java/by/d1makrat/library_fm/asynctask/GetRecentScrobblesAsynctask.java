package by.d1makrat.library_fm.asynctask;

import java.net.URL;
import java.sql.SQLException;
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

public class GetRecentScrobblesAsynctask extends GetScrobblesAsynctask{

    private Exception mException = null;
    private AsynctaskCallback mAsynctaskCallback;

    public GetRecentScrobblesAsynctask(AsynctaskCallback pAsynctaskCallback) {
        mAsynctaskCallback = pAsynctaskCallback;
    }

    @Override
    protected List<Scrobble> doInBackground(String... params) {

        List<Scrobble> scrobbles = new ArrayList<Scrobble>();
        DatabaseWorker databaseWorker = new DatabaseWorker(AppContext.getInstance().getApplicationContext());

        try {
            databaseWorker.openDatabase();

            if (HttpsClient.isNetworkAvailable()) {
                UrlConstructor urlConstructor = new UrlConstructor();
                URL apiRequestUrl = urlConstructor.constructRecentScrobblesApiRequestUrl(params[0], params[1], params[2]);

                HttpsClient httpsClient = new HttpsClient();
                String response = httpsClient.request(apiRequestUrl, "GET");

                JsonParser jsonParser = new JsonParser();

                String errorOrNot = jsonParser.checkForApiErrors(response);
                if (!errorOrNot.equals("No error"))
                    mException = new APIException(errorOrNot);
                else
                    scrobbles = jsonParser.parseScrobbles(response);

                databaseWorker.bulkInsertScrobbles(scrobbles);
            }
            else {
                scrobbles = databaseWorker.getScrobbles(params[0], params[1], params[2]);
            }
        } catch (Exception e) {
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
