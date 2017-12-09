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

public class GetScrobblesOfArtistAsynctask extends GetScrobblesAsynctask{

    private Exception mException = null;
    private GetScrobblesAsynctaskCallback myAsyncTaskCallback;

    public GetScrobblesOfArtistAsynctask(GetScrobblesAsynctaskCallback pAsynctaskCallback) {
        myAsyncTaskCallback = pAsynctaskCallback;
    }

    @Override
    protected List<Scrobble> doInBackground(String... params) {

        List<Scrobble> scrobbles = new ArrayList<Scrobble>();
        URL apiRequestUrl;

        try {
            UrlConstructor urlConstructor = new UrlConstructor();
            apiRequestUrl = urlConstructor.constructScrobblesByArtistApiRequestUrl(params[0], params[1], params[2], params[3]);

            NetworkRequester networkRequester = new NetworkRequester();
            String response = networkRequester.request(apiRequestUrl, "GET");

            JsonParser jsonParser = new JsonParser(response);

            String errorOrNot = jsonParser.checkForApiErrors();
            if (!errorOrNot.equals("No error"))
                mException = new APIException(errorOrNot);
            else
                scrobbles = jsonParser.parseScrobbles();
        } catch (Exception e) {
            e.printStackTrace();
            mException = e;
        }

        return scrobbles;
    }

    @Override
    protected void onPostExecute(List<Scrobble> scrobbles) {
        if (mException != null)
            myAsyncTaskCallback.onException(mException);
        else
            myAsyncTaskCallback.onLoadingScrobblesSuccessful(scrobbles);
    }
}
