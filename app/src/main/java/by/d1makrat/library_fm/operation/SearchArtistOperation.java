package by.d1makrat.library_fm.operation;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import by.d1makrat.library_fm.APIException;
import by.d1makrat.library_fm.https.LastFmRestApiService;
import by.d1makrat.library_fm.json.JsonParser;
import by.d1makrat.library_fm.model.Artist;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static by.d1makrat.library_fm.Constants.API_NO_ERROR;

public class SearchArtistOperation implements IOperation<List<Artist>> {

    private final String mSearchQuery;
    private final int mPage;
    private final LastFmRestApiService mLastFmRestApiService;

    public SearchArtistOperation(String mSearchQuery, int mPage, LastFmRestApiService pLastFmRestApiService) {
        this.mSearchQuery = mSearchQuery;
        this.mPage = mPage;
        mLastFmRestApiService = pLastFmRestApiService;
    }

    @Override
    public List<Artist> perform() throws Exception {

        List<Artist> artists = new ArrayList<>();

        Response response = mLastFmRestApiService.searchArtist(mSearchQuery, mPage, 10).execute();

        JsonParser jsonParser = new JsonParser();

        if (response.isSuccessful()){
//            Object body = response.body();
//            if (body != null) {
//                String input = body.string();
//                String errorOrNot = jsonParser.checkForApiErrors(input);
//                if (!errorOrNot.equals(API_NO_ERROR))
//                    throw new APIException(errorOrNot);
//                else
//                    artists = jsonParser.parseSearchArtistResults(input);
//            }
        }
        else {
            ResponseBody errorBody = response.errorBody();
            if (errorBody != null) throw new UnknownHostException(errorBody.string());
        }

        return artists;
    }
}
