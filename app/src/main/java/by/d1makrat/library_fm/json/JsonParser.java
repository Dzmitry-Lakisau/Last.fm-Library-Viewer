package by.d1makrat.library_fm.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import by.d1makrat.library_fm.model.Date;
import by.d1makrat.library_fm.model.Scrobble;

public class JsonParser {

    public String checkForApiErrors(String pStringToParse) throws JSONException {

        JSONObject obj = new JSONObject(pStringToParse);
        if (obj.has("error")) {
            return obj.getString("message");
        } else return "No error";
    }

    public List<Scrobble> parseScrobbles(String pStringToParse) {

        List<Scrobble> scrobbles = new ArrayList<Scrobble>();

        JSONObject jsonObject;

        try {
            JSONObject obj = new JSONObject(pStringToParse);

            JSONArray tracksJsonArray;
            if (obj.has("recenttracks"))
                tracksJsonArray = obj.getJSONObject("recenttracks").getJSONArray("track");
            else
                tracksJsonArray = obj.getJSONObject("artisttracks").getJSONArray("track");

            for (int i = 0; i < tracksJsonArray.length(); i++) {
                JSONObject trackJsonObject = tracksJsonArray.getJSONObject(i);

                if (!trackJsonObject.has("@attr")) {
                    Scrobble scrobble = new Scrobble();

                    jsonObject = trackJsonObject.optJSONObject("artist");
                    scrobble.setArtist(jsonObject.optString("#text"));

                    scrobble.setTrackTitle(trackJsonObject.getString("name"));

                    jsonObject = trackJsonObject.optJSONObject("album");
                    scrobble.setAlbum(jsonObject.optString("#text"));

                    JSONArray jsonArray = trackJsonObject.optJSONArray("image");
                    String imageUri = jsonArray.optJSONObject(3).getString("#text");
                    scrobble.setImageUri(imageUri.equals("") ? null : imageUri);

                    jsonObject = trackJsonObject.optJSONObject("date");
                    Date date = new Date();
//                    date.setFormattedDate(jsonObject.optString("#text"));
                    date.setUnixDate(jsonObject.optLong("uts"));
                    scrobble.setDate(date);

                    scrobbles.add(scrobble);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return scrobbles;
    }

    public String parseSessionkey(String pStringToParse) {
        String sessionkey = null;

        try {
            JSONObject obj = new JSONObject(pStringToParse);
            sessionkey = obj.getJSONObject("session").getString("key");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return sessionkey;
    }
}
