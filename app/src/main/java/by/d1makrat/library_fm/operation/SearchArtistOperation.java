package by.d1makrat.library_fm.operation;

import java.net.URL;
import java.util.List;

import by.d1makrat.library_fm.APIException;
import by.d1makrat.library_fm.https.HttpsClient;
import by.d1makrat.library_fm.https.RequestMethod;
import by.d1makrat.library_fm.json.JsonParser;
import by.d1makrat.library_fm.model.Artist;
import by.d1makrat.library_fm.utils.UrlConstructor;

import static by.d1makrat.library_fm.Constants.API_NO_ERROR;

public class SearchArtistOperation implements IOperation<List<Artist>> {

    private final String mSearchQuery;
    private final int mPage;

    public SearchArtistOperation(String mSearchQuery, int mPage) {
        this.mSearchQuery = mSearchQuery;
        this.mPage = mPage;
    }

    @Override
    public List<Artist> perform() throws Exception {

        UrlConstructor urlConstructor = new UrlConstructor();
        URL apiRequestUrl = urlConstructor.constructSearchArtistApiRequestUrl(mSearchQuery, mPage);

        HttpsClient httpsClient = new HttpsClient();
        String response = httpsClient.request(apiRequestUrl, RequestMethod.GET);

        JsonParser jsonParser = new JsonParser();

        String errorOrNot = jsonParser.checkForApiErrors(response);
        if (!errorOrNot.equals(API_NO_ERROR))
            throw new APIException(errorOrNot);
        else
            return jsonParser.parseSearchArtistResults(response);
    }
}
