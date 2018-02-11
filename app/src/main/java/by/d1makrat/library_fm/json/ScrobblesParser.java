package by.d1makrat.library_fm.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import by.d1makrat.library_fm.json.model.ScrobbleJson;
import by.d1makrat.library_fm.model.Scrobble;

import static by.d1makrat.library_fm.Constants.JsonConstants.ATTRIBUTE_KEY;
import static by.d1makrat.library_fm.Constants.TRACK_KEY;

public class ScrobblesParser {

    private static final String RECENT_TRACKS_KEY = "recenttracks";
    private static final String ARTIST_TRACKS_KEY = "artisttracks";
    private final String mStringToParse;

    public ScrobblesParser(String pStringToParse) {
        mStringToParse = pStringToParse;
    }

    public List<Scrobble> parse() throws JSONException {
        if (mStringToParse.contains(RECENT_TRACKS_KEY)) {
            return recentScrobbles(new JSONObject(mStringToParse));
        } else {
            return artistScrobbles(new JSONObject(mStringToParse));
        }
    }

    private List<Scrobble> artistScrobbles(JSONObject rootJsonObject) throws JSONException {
        return parseJsonArray(rootJsonObject.getJSONObject(ARTIST_TRACKS_KEY).getJSONArray(TRACK_KEY));
    }

    private List<Scrobble> recentScrobbles(JSONObject rootJsonObject) throws JSONException {
        return parseJsonArray(rootJsonObject.getJSONObject(RECENT_TRACKS_KEY).getJSONArray(TRACK_KEY));
    }

    private List<Scrobble> parseJsonArray(JSONArray scrobblesJsonArray) throws JSONException {

        List<Scrobble> scrobbles = new ArrayList<>();

        for (int i = 0; i < scrobblesJsonArray.length(); i++) {
            JSONObject scrobbleJsonObject = scrobblesJsonArray.getJSONObject(i);

            if (!scrobbleJsonObject.has(ATTRIBUTE_KEY)) {//TODO parse and show scrobble that "now playing"
                Scrobble scrobble = new Scrobble();

                ScrobbleJson scrobbleJson = new ScrobbleJson(scrobbleJsonObject);

                scrobble.setTrackTitle(scrobbleJson.getTrack());
                scrobble.setArtist(scrobbleJson.getArtist());
                scrobble.setAlbum(scrobbleJson.getAlbum());
                scrobble.setImageUri(scrobbleJson.getImageUrl());
                scrobble.setDate(scrobbleJson.getDate());

                scrobbles.add(scrobble);
            }
        }

        return scrobbles;
    }
}
