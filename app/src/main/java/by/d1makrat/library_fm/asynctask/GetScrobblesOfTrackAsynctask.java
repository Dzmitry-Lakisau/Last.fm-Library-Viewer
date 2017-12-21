package by.d1makrat.library_fm.asynctask;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import by.d1makrat.library_fm.APIException;
import by.d1makrat.library_fm.AsynctaskCallback;
import by.d1makrat.library_fm.NetworkRequester;
import by.d1makrat.library_fm.UrlConstructor;
import by.d1makrat.library_fm.json.JsonParser;
import by.d1makrat.library_fm.model.Scrobble;

public class GetScrobblesOfTrackAsynctask extends GetScrobblesAsynctask {

    private final AsynctaskCallback mAsynctaskCallback;
    private Exception mException;

    public GetScrobblesOfTrackAsynctask(AsynctaskCallback pAsynctaskCallback) {
        mAsynctaskCallback = pAsynctaskCallback;
    }

    @Override
    protected List<Scrobble> doInBackground(String... params) {

        List<Scrobble> artistScrobbles = new ArrayList<Scrobble>();
        List<Scrobble> trackScrobbles = new ArrayList<Scrobble>();
        String response;

        String artist = params[0];
        String track = params[1];
        String from = params[2];
        String to = params[3];

        try {
            int page = 1;
            do{
                UrlConstructor urlConstructor = new UrlConstructor();
                URL apiRequestUrl = urlConstructor.constructScrobblesByArtistApiRequestUrl(artist, String.valueOf(page), from, to);

                NetworkRequester networkRequester = new NetworkRequester();
                response = networkRequester.request(apiRequestUrl, "GET");

                JsonParser jsonParser = new JsonParser();
                String errorOrNot = jsonParser.checkForApiErrors(response);
                if (!errorOrNot.equals("No error"))
                    mException = new APIException(errorOrNot);
                else
                    artistScrobbles = jsonParser.parseScrobbles(response);

                for(Scrobble scrobble : artistScrobbles){
                    if(scrobble.getTrackTitle().equals(track)){
                        trackScrobbles.add(scrobble);
                    }
                }

                page++;
            }
            while(response.contains("name"));

        } catch (Exception e) {
            e.printStackTrace();
            mException = e;
        }

        return trackScrobbles;
    }

    @Override
    protected void onPostExecute(List<Scrobble> scrobbles) {
        if (mException != null)
            mAsynctaskCallback.onException(mException);
        else
            mAsynctaskCallback.onLoadingScrobblesSuccessful(scrobbles);
    }
}