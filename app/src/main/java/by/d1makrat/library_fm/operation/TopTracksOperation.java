package by.d1makrat.library_fm.operation;

import com.google.gson.GsonBuilder;

import java.net.URL;
import java.util.List;

import by.d1makrat.library_fm.APIException;
import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.https.HttpsClient;
import by.d1makrat.library_fm.https.RequestMethod;
import by.d1makrat.library_fm.json.JsonParser;
import by.d1makrat.library_fm.json.TopTracksAdapter;
import by.d1makrat.library_fm.model.TopTracks;
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
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(TopTracks.class, new TopTracksAdapter());
                topTracks = gsonBuilder.create().fromJson(response, TopTracks.class).getAlbums();
                topTracksCount = gsonBuilder.create().fromJson(response, TopTracks.class).getTotal();

                if (mPage == 1) {
                    AppContext.getInstance().getAppDatabase().topTracksDao().deleteTracks(period);
                }
                for (Track track : topTracks) {
                    track.setPeriod(period);
                }
                AppContext.getInstance().getAppDatabase().topTracksDao().insertTracks(topTracks);
            }
        }
        else {
            topTracks = AppContext.getInstance().getAppDatabase().topTracksDao().getTracks(period, mPage, AppContext.getInstance().getLimit());
            topTracksCount = AppContext.getInstance().getAppDatabase().topTracksDao().getTracksCount(period);
        }

        return new TopOperationResult<>(topTracks, topTracksCount);
    }
}
