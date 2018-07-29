package by.d1makrat.library_fm.operation;

import java.net.URL;
import java.util.List;

import by.d1makrat.library_fm.APIException;
import by.d1makrat.library_fm.database.DatabaseWorker;
import by.d1makrat.library_fm.https.HttpsClient;
import by.d1makrat.library_fm.https.RequestMethod;
import by.d1makrat.library_fm.json.JsonParser;
import by.d1makrat.library_fm.model.Track;
import by.d1makrat.library_fm.operation.model.TopOperationResult;
import by.d1makrat.library_fm.utils.UrlConstructor;

import static by.d1makrat.library_fm.Constants.API_NO_ERROR;

public class TopTracksOperation implements IOperation<TopOperationResult<Track>> {

    private final String period;
    private final int mPage;

    public TopTracksOperation(String period, int mPage) {
        this.period = period;
        this.mPage = mPage;
    }

    @Override
    public TopOperationResult<Track> perform() throws Exception {

        List<Track> topTracks;
        String topTracksCount;
        DatabaseWorker databaseWorker = new DatabaseWorker();

        try {
            databaseWorker.openDatabase();

            if (HttpsClient.isNetworkAvailable()) {
                UrlConstructor urlConstructor = new UrlConstructor();
                URL apiRequestUrl = urlConstructor.constructTopTracksApiRequestUrl(period, mPage);

                HttpsClient httpsClient = new HttpsClient();
                String response = httpsClient.request(apiRequestUrl, RequestMethod.GET);

                JsonParser jsonParser = new JsonParser();
                String errorOrNot = jsonParser.checkForApiErrors(response);

                if (!errorOrNot.equals(API_NO_ERROR)) {
                    throw new APIException(errorOrNot);
                }
                else{
                   // TopsParser topsParser = new TopsParser(response);
                    topTracks = null;// topsParser.parseUserTopTracks();

                    if (mPage == 1) {
                        databaseWorker.deleteTopTracks(period);
                    }
                    databaseWorker.getTopTracksTable().bulkInsertTopTracks(topTracks, period);

                    topTracksCount = null;// topsParser.parseTracksCount();
                }
            }
            else {
                topTracks = databaseWorker.getTopTracksTable().getTopTracks(period, mPage);

                topTracksCount = databaseWorker.getTopTracksTable().getTracksCount(period);
            }
        } finally {
            databaseWorker.closeDatabase();
        }

        return new TopOperationResult<>(topTracks, topTracksCount);
    }
}
