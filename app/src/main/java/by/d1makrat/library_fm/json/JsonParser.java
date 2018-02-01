package by.d1makrat.library_fm.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.model.Artist;

import static by.d1makrat.library_fm.Constants.API_NO_ERROR;
import static by.d1makrat.library_fm.Constants.ARTIST_KEY;
import static by.d1makrat.library_fm.Constants.EMPTY_STRING;
import static by.d1makrat.library_fm.Constants.JsonConstants.IMAGE_KEY;
import static by.d1makrat.library_fm.Constants.JsonConstants.TEXT_KEY;
import static by.d1makrat.library_fm.Constants.NAME_KEY;
import static by.d1makrat.library_fm.Constants.URL_KEY;

public class JsonParser {

    private static final int MAX_IMAGE_RESOLUTION_INDEX = 4;

    public String checkForApiErrors(String pStringToParse) throws JSONException {

        JSONObject obj = new JSONObject(pStringToParse);
        if (obj.has("error"))
            return obj.getString("message");
        else
            return API_NO_ERROR;
    }

    public String parseSessionkey(String pStringToParse) throws JSONException {
        return (new JSONObject(pStringToParse)).getJSONObject("session").getString("key");
    }

    public List<Artist> parseSearchArtistResults(String pStringToParse) throws JSONException {
        List<Artist> artists = new ArrayList<>();

        JSONArray artistsJsonArray = (new JSONObject(pStringToParse)).getJSONObject("results").getJSONObject("artistmatches").getJSONArray(ARTIST_KEY);

        for (int i = 0; i < artistsJsonArray.length(); i++) {
            JSONObject artistJsonObject = artistsJsonArray.getJSONObject(i);

            Artist artist = new Artist();

            artist.setName(artistJsonObject.getString(NAME_KEY));

            artist.setListenersCount(artistJsonObject.getString("listeners"));

            artist.setUrl(artistJsonObject.getString(URL_KEY));

            JSONArray jsonArray = artistJsonObject.getJSONArray(IMAGE_KEY);
            String imageUri = jsonArray.getJSONObject(MAX_IMAGE_RESOLUTION_INDEX).getString(TEXT_KEY);
            artist.setImageUri(imageUri.equals(EMPTY_STRING) ? null : imageUri);

            artists.add(artist);
        }

        return artists;
    }

    public String parseSendScrobbleResult(String pStringToParse) throws JSONException {

        String message;

        JSONObject messageJsonObject = (new JSONObject(pStringToParse)).getJSONObject("scrobbles").getJSONObject("scrobble").getJSONObject("ignoredMessage");
        message = messageJsonObject.getString("code");
        switch (message){
            case "0":
                return AppContext.getInstance().getString(R.string.manual_fragment_scrobble_accepted);
            case "2":
                return AppContext.getInstance().getString(R.string.manual_fragment_track_ignored);
            case "3":
                return AppContext.getInstance().getString(R.string.manual_fragment_timestamp_old);
            case "4":
                return AppContext.getInstance().getString(R.string.manual_fragment_timestamp_new);
            case "5":
                return AppContext.getInstance().getString(R.string.manual_fragment_limit_exceeded);
            default:
                return AppContext.getInstance().getString(R.string.manual_fragment_ignored_message);
        }
    }
}