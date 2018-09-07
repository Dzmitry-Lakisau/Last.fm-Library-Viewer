package by.d1makrat.library_fm.operation;

import com.google.gson.GsonBuilder;

import java.net.URL;
import java.util.List;

import by.d1makrat.library_fm.APIException;
import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.https.HttpsClient;
import by.d1makrat.library_fm.https.RequestMethod;
import by.d1makrat.library_fm.json.JsonParser;
import by.d1makrat.library_fm.json.TopArtistsAdapter;
import by.d1makrat.library_fm.model.Artist;
import by.d1makrat.library_fm.model.TopArtists;
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
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(TopArtists.class, new TopArtistsAdapter());
                topArtists = gsonBuilder.create().fromJson(response, TopArtists.class).getAlbums();
                topArtistsCount = gsonBuilder.create().fromJson(response, TopArtists.class).getTotal();

                if (mPage == 1) {
                    AppContext.getInstance().getAppDatabase().topArtistsDao().deleteArtists(period);
                }
                for (Artist artist : topArtists) {
                    artist.setPeriod(period);
                }
                AppContext.getInstance().getAppDatabase().topArtistsDao().insertArtists(topArtists);
            }
        }
        else {
            topArtists = AppContext.getInstance().getAppDatabase().topArtistsDao().getArtists(period, mPage, AppContext.getInstance().getLimit());
            topArtistsCount = AppContext.getInstance().getAppDatabase().topArtistsDao().getArtistsCount(period);
        }

        return new TopOperationResult<>(topArtists, topArtistsCount);
    }
}
