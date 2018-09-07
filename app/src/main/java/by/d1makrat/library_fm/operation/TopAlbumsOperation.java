package by.d1makrat.library_fm.operation;

import com.google.gson.GsonBuilder;

import java.net.URL;
import java.util.List;

import by.d1makrat.library_fm.APIException;
import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.https.HttpsClient;
import by.d1makrat.library_fm.https.RequestMethod;
import by.d1makrat.library_fm.json.JsonParser;
import by.d1makrat.library_fm.json.TopAlbumsAdapter;
import by.d1makrat.library_fm.model.Album;
import by.d1makrat.library_fm.model.TopAlbums;
import by.d1makrat.library_fm.operation.model.TopOperationResult;
import by.d1makrat.library_fm.utils.UrlConstructor;

import static by.d1makrat.library_fm.Constants.API_NO_ERROR;

public class TopAlbumsOperation implements IOperation<TopOperationResult<Album>> {

    private final String period;
    private final int mPage;

    public TopAlbumsOperation(String period, int mPage) {
        this.period = period;
        this.mPage = mPage;
    }

    @Override
    public TopOperationResult<Album> perform() throws Exception {

        List<Album> topAlbums;
        String topAlbumsCount;

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
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(TopAlbums.class, new TopAlbumsAdapter());
                topAlbums = gsonBuilder.create().fromJson(response, TopAlbums.class).getAlbums();
                topAlbumsCount = gsonBuilder.create().fromJson(response, TopAlbums.class).getTotal();

                if (mPage == 1) {
                    AppContext.getInstance().getAppDatabase().topAlbumsDao().deleteAlbums(period);
                }
                for (Album album : topAlbums) {
                    album.setPeriod(period);
                }
                AppContext.getInstance().getAppDatabase().topAlbumsDao().insertAlbums(topAlbums);
            }
        }
        else {
            topAlbums = AppContext.getInstance().getAppDatabase().topAlbumsDao().getAlbums(period, mPage, AppContext.getInstance().getLimit());
            topAlbumsCount = AppContext.getInstance().getAppDatabase().topAlbumsDao().getAlbumsCount(period);
        }

        return new TopOperationResult<>(topAlbums, topAlbumsCount);
    }
}
