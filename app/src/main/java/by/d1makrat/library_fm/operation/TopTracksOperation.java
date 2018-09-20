package by.d1makrat.library_fm.operation;

import com.google.gson.GsonBuilder;

import java.net.URL;

import by.d1makrat.library_fm.APIException;
import by.d1makrat.library_fm.database.DatabaseWorker;
import by.d1makrat.library_fm.https.HttpsClient;
import by.d1makrat.library_fm.https.RequestMethod;
import by.d1makrat.library_fm.json.JsonParser;
import by.d1makrat.library_fm.json.TopTracksAdapter;
import by.d1makrat.library_fm.model.TopTracks;
import by.d1makrat.library_fm.utils.UrlConstructor;

import static by.d1makrat.library_fm.Constants.API_NO_ERROR;

public class TopTracksOperation implements IOperation<TopTracks> {

    private final String period;
    private final int mPage;

    public TopTracksOperation(String period, int mPage) {
        this.period = period;
        this.mPage = mPage;
    }

    @Override
    public TopTracks perform() throws Exception {
        TopTracks topTracks;
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
                    GsonBuilder builder = new GsonBuilder();
                    builder.registerTypeAdapter(TopTracks.class, new TopTracksAdapter());
                    topTracks = builder.create().fromJson(response, TopTracks.class);

                    if (mPage == 1) {
                        databaseWorker.deleteTopTracks(period);
                    }
                    databaseWorker.getTopTracksTable().bulkInsertTopTracks(topTracks.getTracks(), period);

                }
            }
            else {
                topTracks = new TopTracks(databaseWorker.getTopTracksTable().getTopTracks(period, mPage), databaseWorker.getTopTracksTable().getTracksCount(period));
            }
        } finally {
            databaseWorker.closeDatabase();
        }

        return topTracks;
    }
}
