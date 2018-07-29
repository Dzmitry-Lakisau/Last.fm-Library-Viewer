package by.d1makrat.library_fm.operation;

import java.net.URL;
import java.util.List;

import by.d1makrat.library_fm.APIException;
import by.d1makrat.library_fm.database.DatabaseWorker;
import by.d1makrat.library_fm.https.HttpsClient;
import by.d1makrat.library_fm.https.RequestMethod;
import by.d1makrat.library_fm.json.JsonParser;
import by.d1makrat.library_fm.model.Artist;
import by.d1makrat.library_fm.operation.model.TopOperationResult;
import by.d1makrat.library_fm.utils.UrlConstructor;

import static by.d1makrat.library_fm.Constants.API_NO_ERROR;

public class TopArtistsOperation implements IOperation<TopOperationResult<Artist>> {

    private final String period;
    private final int mPage;

    public TopArtistsOperation(String period, int mPage) {
        this.period = period;
        this.mPage = mPage;
    }

    @Override
    public TopOperationResult<Artist> perform() throws Exception {
        List<Artist> topArtists;
        String topArtistsCount;
        DatabaseWorker databaseWorker = new DatabaseWorker();

        try {
            databaseWorker.openDatabase();

            if (HttpsClient.isNetworkAvailable()) {
                UrlConstructor urlConstructor = new UrlConstructor();
                URL apiRequestUrl = urlConstructor.constructTopArtistsApiRequestUrl(period, mPage);

                HttpsClient httpsClient = new HttpsClient();
                String response = httpsClient.request(apiRequestUrl, RequestMethod.GET);

                JsonParser jsonParser = new JsonParser();
                String errorOrNot = jsonParser.checkForApiErrors(response);

                if (!errorOrNot.equals(API_NO_ERROR)) {
                    throw new APIException(errorOrNot);
                }
                else{
                    //TopsParser topsParser = new TopsParser(response);
                    topArtists = null;// topsParser.parseUserTopArtists();

                    if (mPage == 1) {
                        databaseWorker.deleteTopArtists(period);
                    }
                    databaseWorker.getTopArtistsTable().bulkInsertTopArtists(topArtists, period);

                    topArtistsCount = null;// topsParser.parseArtistsCount();
                }
            }
            else {
                topArtists = databaseWorker.getTopArtistsTable().getTopArtists(period, mPage);
                topArtistsCount = databaseWorker.getTopArtistsTable().getArtistsCount(period);
            }
        } finally {
            databaseWorker.closeDatabase();
        }

        return new TopOperationResult<>(topArtists, topArtistsCount);
    }
}
