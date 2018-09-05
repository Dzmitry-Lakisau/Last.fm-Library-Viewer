package by.d1makrat.library_fm.operation;

import com.google.gson.GsonBuilder;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import by.d1makrat.library_fm.APIException;
import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.database.DatabaseWorker;
import by.d1makrat.library_fm.https.HttpsClient;
import by.d1makrat.library_fm.https.RequestMethod;
import by.d1makrat.library_fm.json.JsonParser;
import by.d1makrat.library_fm.json.ScrobblesAdapter;
import by.d1makrat.library_fm.json.model.ScrobblesJsonModel;
import by.d1makrat.library_fm.model.Scrobble;
import by.d1makrat.library_fm.utils.UrlConstructor;

import static by.d1makrat.library_fm.Constants.API_NO_ERROR;
import static by.d1makrat.library_fm.Constants.NAME_KEY;

public class ScrobblesOfAlbumOperation implements IOperation<List<Scrobble>> {

    private final String artist;
    private final String album;
    private final Long mFrom;
    private final Long mTo;

    public ScrobblesOfAlbumOperation(String artist, String album, Long mFrom, Long mTo) {
        this.artist = artist;
        this.album = album;
        this.mFrom = mFrom;
        this.mTo = mTo;
    }

    @Override
    public List<Scrobble> perform() throws Exception {

        List<Scrobble> albumScrobbles = new ArrayList<>();
        String response;
        DatabaseWorker databaseWorker = new DatabaseWorker();

        try {
            databaseWorker.openDatabase();

            if (HttpsClient.isNetworkAvailable()) {
                int page = 1;
                do {
                    UrlConstructor urlConstructor = new UrlConstructor();
                    URL apiRequestUrl = urlConstructor.constructScrobblesOfArtistApiRequestUrl(artist, page, mFrom, mTo);

                    HttpsClient httpsClient = new HttpsClient();
                    response = httpsClient.request(apiRequestUrl, RequestMethod.GET);

                    JsonParser jsonParser = new JsonParser();
                    String errorOrNot = jsonParser.checkForApiErrors(response);
                    if (!errorOrNot.equals(API_NO_ERROR)) {
                        throw new APIException(errorOrNot);
                    }
                    else {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.registerTypeAdapter(ScrobblesJsonModel.class, new ScrobblesAdapter());
                        List<Scrobble> artistScrobbles = gsonBuilder.create().fromJson(response, ScrobblesJsonModel.class).getAll();

                        AppContext.getInstance().getAppDatabase().scrobblesDao().insert(artistScrobbles);

                        for (Scrobble scrobble : artistScrobbles) {
                            if(scrobble.getTrackTitle() != null) {
                                if (scrobble.getAlbum().equals(album)) {
                                    albumScrobbles.add(scrobble);
                                }
                            }
                        }
                    }

                    page++;
                }
                while (response.contains(NAME_KEY));
            }
            else {
                albumScrobbles = AppContext.getInstance().getAppDatabase().scrobblesDao().getScrobblesOfAlbum(artist, album);
            }
        } finally {
            databaseWorker.closeDatabase();
        }

        return albumScrobbles;
    }
}
