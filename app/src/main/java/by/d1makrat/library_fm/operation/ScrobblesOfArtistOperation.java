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

public class ScrobblesOfArtistOperation implements IOperation<List<Scrobble>> {

    private String artist;
    private int mPage;
    private Long mFrom, mTo;

    public ScrobblesOfArtistOperation(String artist, int mPage, Long mFrom, Long mTo) {
        this.artist = artist;
        this.mPage = mPage;
        this.mFrom = mFrom;
        this.mTo = mTo;
    }

    @Override
    public List<Scrobble> perform() throws Exception {
        List<Scrobble> artistScrobbles;
        URL apiRequestUrl;
        DatabaseWorker databaseWorker = new DatabaseWorker();

        try {
            databaseWorker.openDatabase();

            if (HttpsClient.isNetworkAvailable()) {
                UrlConstructor urlConstructor = new UrlConstructor();
                apiRequestUrl = urlConstructor.constructScrobblesOfArtistApiRequestUrl(artist, mPage, mFrom, mTo);

                HttpsClient httpsClient = new HttpsClient();
                String response = httpsClient.request(apiRequestUrl, RequestMethod.GET);

                JsonParser jsonParser = new JsonParser();

                String errorOrNot = jsonParser.checkForApiErrors(response);
                if (!errorOrNot.equals(API_NO_ERROR)) {
                    throw new APIException(errorOrNot);
                }
                else {
                    ScrobblesParser scrobblesParser = new ScrobblesParser(response);
                    artistScrobbles = scrobblesParser.parse();

                    databaseWorker.getScrobblesTable().bulkInsertScrobbles(artistScrobbles);
                }
            }
            else {
                artistScrobbles = databaseWorker.getScrobblesTable().getScrobblesOfArtist(artist, mPage, mFrom, mTo);
            }
        } finally {
            databaseWorker.closeDatabase();
        }

        return artistScrobbles;
    }
}
