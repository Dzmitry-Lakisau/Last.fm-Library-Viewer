package by.d1makrat.library_fm.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import by.d1makrat.library_fm.model.Date;
import by.d1makrat.library_fm.model.Scrobble;

public class ScrobblesParser {
    
    private String mStringToParse; 
    
    public ScrobblesParser(String stringToParse){
              mStringToParse = stringToParse;
    }

    public String checkForApiErrors() throws JSONException{

        JSONObject obj = new JSONObject(mStringToParse);
        if (obj.has("error")) {
            return obj.getString("message");
        }
        else return "No error";
    }

    public List<Scrobble> parseTracks() {

        List<Scrobble> scrobbles = new ArrayList<Scrobble>();

        JSONObject jsonObject;

        try {
            JSONObject obj = new JSONObject(mStringToParse);
            JSONArray tracksJsonArray = obj.getJSONObject("recenttracks").getJSONArray("track");

            for (int i=0; i<tracksJsonArray.length(); i++) {
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
}
