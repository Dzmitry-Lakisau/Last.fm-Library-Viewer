package by.d1makrat.library_fm.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import by.d1makrat.library_fm.model.Scrobble;

import static by.d1makrat.library_fm.Constants.ALBUM_KEY;
import static by.d1makrat.library_fm.Constants.ARTIST_KEY;
import static by.d1makrat.library_fm.Constants.TRACK_KEY;

public class ScrobblesParser {

    private String mStringToParse;

    public ScrobblesParser(String pStringToParse) {
        mStringToParse = pStringToParse;
    }

    public List<Scrobble> parse() throws JSONException {

        List<Scrobble> scrobbles = new ArrayList<>();

        JSONObject source = new JSONObject(mStringToParse);

        JSONArray tracksJsonArray;
        if (source.has("recenttracks"))
            tracksJsonArray = source.getJSONObject("recenttracks").getJSONArray(TRACK_KEY);
        else
            tracksJsonArray = source.getJSONObject("artisttracks").getJSONArray(TRACK_KEY);

        for (int i = 0; i < tracksJsonArray.length(); i++) {
            JSONObject trackJsonObject = tracksJsonArray.getJSONObject(i);

            if (!trackJsonObject.has("@attr")) {//TODO ?
                Scrobble scrobble = new Scrobble();

                JSONObject jsonObject = trackJsonObject.getJSONObject(ARTIST_KEY);
                scrobble.setArtist(jsonObject.getString("#text"));

                scrobble.setTrackTitle(trackJsonObject.getString("name"));

                jsonObject = trackJsonObject.getJSONObject(ALBUM_KEY);
                scrobble.setAlbum(jsonObject.getString("#text"));

                JSONArray jsonArray = trackJsonObject.getJSONArray("image");
                String imageUri = jsonArray.getJSONObject(3).getString("#text");
                scrobble.setImageUri(imageUri.equals("") ? null : imageUri);

                jsonObject = trackJsonObject.getJSONObject("date");
                scrobble.setDate(jsonObject.getLong("uts"));

                scrobbles.add(scrobble);
            }
        }

        return scrobbles;
    }
}
