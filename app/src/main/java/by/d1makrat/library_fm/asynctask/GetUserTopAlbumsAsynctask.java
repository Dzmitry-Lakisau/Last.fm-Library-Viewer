package by.d1makrat.library_fm.asynctask;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import by.d1makrat.library_fm.APIException;
import by.d1makrat.library_fm.GetScrobblesAsynctaskCallback;
import by.d1makrat.library_fm.NetworkRequester;
import by.d1makrat.library_fm.UrlConstructor;
import by.d1makrat.library_fm.json.JsonParser;
import by.d1makrat.library_fm.model.Scrobble;
import by.d1makrat.library_fm.ui.fragment.TopAlbumsFragment;

public class GetUserTopAlbumsAsynctask extends GetScrobblesAsynctask {

    private Exception mException;
    private GetScrobblesAsynctaskCallback asynctaskCallback;

    public GetUserTopAlbumsAsynctask(GetScrobblesAsynctaskCallback pAsynctaskCallback) {
        asynctaskCallback = pAsynctaskCallback;
    }

    @Override
    protected List<Scrobble> doInBackground(String... params) {
        List<Scrobble> scrobbles = new ArrayList<Scrobble>();
        URL apiRequestUrl;

        try {
            UrlConstructor urlConstructor = new UrlConstructor();
            apiRequestUrl = urlConstructor.constructGetUserTopAlbumsApiRequestUrl(params[0], params[1]);

            NetworkRequester networkRequester = new NetworkRequester();
            String response = networkRequester.request(apiRequestUrl, "GET");

            JsonParser jsonParser = new JsonParser();

            String errorOrNot = jsonParser.checkForApiErrors(response);
            if (!errorOrNot.equals("No error"))
                mException = new APIException(errorOrNot);
            else
                scrobbles = jsonParser.parseScrobbles(response);
        } catch (Exception e) {
            e.printStackTrace();
            mException = e;
        }

        return scrobbles;
    }

    @Override
    protected void onPostExecute(List<Scrobble> scrobbles) {
        if (mException != null)
            asynctaskCallback.onException(mException);
        else
            asynctaskCallback.onLoadingScrobblesSuccessful(scrobbles);
    }
}