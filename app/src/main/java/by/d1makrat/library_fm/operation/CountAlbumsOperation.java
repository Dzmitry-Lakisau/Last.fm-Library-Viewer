package by.d1makrat.library_fm.operation;

import java.net.URL;
import java.util.List;

import by.d1makrat.library_fm.APIException;
import by.d1makrat.library_fm.database.DatabaseWorker;
import by.d1makrat.library_fm.https.HttpsClient;
import by.d1makrat.library_fm.https.RequestMethod;
import by.d1makrat.library_fm.json.JsonParser;
import by.d1makrat.library_fm.json.TopsParser;
import by.d1makrat.library_fm.model.TopAlbum;
import by.d1makrat.library_fm.utils.UrlConstructor;

import static by.d1makrat.library_fm.Constants.API_NO_ERROR;

public class CountAlbumsOperation implements IOperation<List<TopAlbum>> {

    private String period;
    private int mPage;

    public CountAlbumsOperation(String period, int mPage) {
        this.period = period;
        this.mPage = mPage;
    }

    @Override
    public List<TopAlbum> perform() throws Exception {

        List<TopAlbum> topAlbums;
        DatabaseWorker databaseWorker = new DatabaseWorker();

        try {
            databaseWorker.openDatabase();

            if (HttpsClient.isNetworkAvailable()) {
                UrlConstructor urlConstructor = new UrlConstructor();
                URL apiRequestUrl = urlConstructor.constructGetUserTopAlbumsApiRequestUrl(period, mPage);

                HttpsClient httpsClient = new HttpsClient();
                String response = httpsClient.request(apiRequestUrl, RequestMethod.GET);

                JsonParser jsonParser = new JsonParser();
                String errorOrNot = jsonParser.checkForApiErrors(response);

                if (!errorOrNot.equals(API_NO_ERROR)) {
                    throw new APIException(errorOrNot);
                }
                else{
                    TopsParser topsParser = new TopsParser(response);
                    topAlbums = topsParser.parseUserTopAlbums();

                    if (mPage == 1) {
                        databaseWorker.deleteTopAlbums(period);
                    }
                    databaseWorker.getTopAlbumsTable().bulkInsertTopAlbums(topAlbums, period);
                }
            }
            else {
                topAlbums = databaseWorker.getTopAlbumsTable().getTopAlbums(period, mPage);
            }
        } finally {
            databaseWorker.closeDatabase();
        }

        return topAlbums;
    }
}