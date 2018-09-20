package by.d1makrat.library_fm.operation;

import com.google.gson.GsonBuilder;

import java.net.URL;
import java.util.List;

import by.d1makrat.library_fm.APIException;
import by.d1makrat.library_fm.database.DatabaseWorker;
import by.d1makrat.library_fm.https.HttpsClient;
import by.d1makrat.library_fm.https.RequestMethod;
import by.d1makrat.library_fm.json.JsonParser;
import by.d1makrat.library_fm.json.TopArtistsAdapter;
import by.d1makrat.library_fm.model.Artist;
import by.d1makrat.library_fm.model.TopArtists;
import by.d1makrat.library_fm.operation.model.TopOperationResult;
import by.d1makrat.library_fm.utils.UrlConstructor;

import static by.d1makrat.library_fm.Constants.API_NO_ERROR;

public class TopArtistsOperation implements IOperation<TopArtists> {

    private final String period;
    private final int mPage;

    public TopArtistsOperation(String period, int mPage) {
        this.period = period;
        this.mPage = mPage;
    }

    @Override
    public TopArtists perform() throws Exception {
        TopArtists topArtists;
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
                    GsonBuilder builder = new GsonBuilder();
                    builder.registerTypeAdapter(TopArtists.class, new TopArtistsAdapter());
                    topArtists = builder.create().fromJson(response, TopArtists.class);

                    if (mPage == 1) {
                        databaseWorker.deleteTopArtists(period);
                    }
                    databaseWorker.getTopArtistsTable().bulkInsertTopArtists(topArtists.getArtists(), period);

                }
            }
            else {
                topArtists = new TopArtists(databaseWorker.getTopArtistsTable().getTopArtists(period, mPage), databaseWorker.getTopArtistsTable().getArtistsCount(period));
            }
        } finally {
            databaseWorker.closeDatabase();
        }

        return topArtists;
    }
}
