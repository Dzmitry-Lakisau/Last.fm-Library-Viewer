package by.d1makrat.library_fm.operation;

import com.google.gson.GsonBuilder;

import java.net.URL;

import by.d1makrat.library_fm.APIException;
import by.d1makrat.library_fm.database.DatabaseWorker;
import by.d1makrat.library_fm.https.HttpsClient;
import by.d1makrat.library_fm.https.RequestMethod;
import by.d1makrat.library_fm.json.JsonParser;
import by.d1makrat.library_fm.json.TopAlbumsAdapter;
import by.d1makrat.library_fm.model.TopAlbums;
import by.d1makrat.library_fm.utils.UrlConstructor;

import static by.d1makrat.library_fm.Constants.API_NO_ERROR;

public class TopAlbumsOperation implements IOperation<TopAlbums> {

    private final String period;
    private final int mPage;

    public TopAlbumsOperation(String period, int mPage) {
        this.period = period;
        this.mPage = mPage;
    }

    @Override
    public TopAlbums perform() throws Exception {

        TopAlbums topAlbums;
        DatabaseWorker databaseWorker = new DatabaseWorker();

        try {
            databaseWorker.openDatabase();

            if (HttpsClient.isNetworkAvailable()) {
                UrlConstructor urlConstructor = new UrlConstructor();
                URL apiRequestUrl = urlConstructor.constructTopAlbumsApiRequestUrl(period, mPage);

                HttpsClient httpsClient = new HttpsClient();
                String response = httpsClient.request(apiRequestUrl, RequestMethod.GET);

                JsonParser jsonParser = new JsonParser();
                String errorOrNot = jsonParser.checkForApiErrors(response);

                if (!errorOrNot.equals(API_NO_ERROR)) {
                    throw new APIException(errorOrNot);
                }
                else{
                    GsonBuilder builder = new GsonBuilder();
                    builder.registerTypeAdapter(TopAlbums.class, new TopAlbumsAdapter());
                    topAlbums = builder.create().fromJson(response, TopAlbums.class);

                    if (mPage == 1) {
                        databaseWorker.deleteTopAlbums(period);
                    }
                    databaseWorker.getTopAlbumsTable().bulkInsertTopAlbums(topAlbums.getAlbums(), period);
                }
            }
            else {
                topAlbums = new TopAlbums(databaseWorker.getTopAlbumsTable().getTopAlbums(period, mPage), databaseWorker.getTopAlbumsTable().getAlbumsCount(period));
            }
        } finally {
            databaseWorker.closeDatabase();
        }

        return topAlbums;
    }
}
