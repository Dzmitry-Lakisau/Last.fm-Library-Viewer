package by.d1makrat.library_fm.operation;

import java.net.URL;
import java.util.List;

import by.d1makrat.library_fm.APIException;
import by.d1makrat.library_fm.database.DatabaseWorker;
import by.d1makrat.library_fm.https.HttpsClient;
import by.d1makrat.library_fm.https.RequestMethod;
import by.d1makrat.library_fm.json.JsonParser;
import by.d1makrat.library_fm.json.ScrobblesParser;
import by.d1makrat.library_fm.model.Scrobble;
import by.d1makrat.library_fm.utils.UrlConstructor;

import static by.d1makrat.library_fm.Constants.API_NO_ERROR;

public class RecentScrobblesOperation implements IOperation<List<Scrobble>> {

    private final int  mPage;
    private final Long mFrom;
    private final Long mTo;

    public RecentScrobblesOperation(int mPage, Long mFrom, Long mTo) {
        this.mPage = mPage;
        this.mFrom = mFrom;
        this.mTo = mTo;
    }

    @Override
    public List<Scrobble> perform() throws Exception {

        List<Scrobble> scrobbles;
        DatabaseWorker databaseWorker = new DatabaseWorker();

        try {
            databaseWorker.openDatabase();

            if (HttpsClient.isNetworkAvailable()) {
                UrlConstructor urlConstructor = new UrlConstructor();
                URL apiRequestUrl = urlConstructor.constructRecentScrobblesApiRequestUrl(mPage, mFrom, mTo);

                HttpsClient httpsClient = new HttpsClient();
                String response = httpsClient.request(apiRequestUrl, RequestMethod.GET);

                JsonParser jsonParser = new JsonParser();

                String errorOrNot = jsonParser.checkForApiErrors(response);
                if (!errorOrNot.equals(API_NO_ERROR)) {
                    throw new APIException(errorOrNot);
                } else {
                    ScrobblesParser scrobblesParser = new ScrobblesParser(response);
                    scrobbles = scrobblesParser.parse();

                    databaseWorker.getScrobblesTable().bulkInsertScrobbles(scrobbles);
                }
            } else {
                scrobbles = databaseWorker.getScrobblesTable().getScrobbles(mPage, mFrom, mTo);
            }
        } finally {
            databaseWorker.closeDatabase();
        }

        return scrobbles;
    }
}
