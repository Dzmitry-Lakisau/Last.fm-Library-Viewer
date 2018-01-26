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

public class JsonParser {

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

            artist.setName(artistJsonObject.getString("name"));

            artist.setListenersCount(artistJsonObject.getString("listeners"));

            artist.setUrl(artistJsonObject.getString("url"));

            JSONArray jsonArray = artistJsonObject.getJSONArray("image");
            String imageUri = jsonArray.getJSONObject(4).getString("#text");
            artist.setImageUri(imageUri.equals("") ? null : imageUri);

            artists.add(artist);
        }

        return artists;
    }

    public String parseSendScrobbleResult(String pStringToParse) throws JSONException {

        String message;

        JSONObject messageJsonObject = (new JSONObject(pStringToParse)).getJSONObject("scrobbles").getJSONObject("scrobble").getJSONObject("ignoredMessage");
        message = messageJsonObject.getString("code");
        if (message.equals("0")){
            message = "Accepted";
        }
        else
            message = AppContext.getInstance().getString(R.string.scrobble_ignored_message);

        return message;
    }
}