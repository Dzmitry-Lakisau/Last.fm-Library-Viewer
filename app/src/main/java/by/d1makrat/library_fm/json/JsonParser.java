package by.d1makrat.library_fm.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import by.d1makrat.library_fm.model.RankedItem;
import by.d1makrat.library_fm.model.Scrobble;
import by.d1makrat.library_fm.model.User;

public class JsonParser {

    public String checkForApiErrors(String pStringToParse) throws JSONException {

        JSONObject obj = new JSONObject(pStringToParse);
        if (obj.has("error"))
            return obj.getString("message");
        else
            return "No error";
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

                if (!trackJsonObject.has("@attr")) {//TODO ?
                    Scrobble scrobble = new Scrobble();

                    jsonObject = trackJsonObject.getJSONObject("artist");
                    scrobble.setArtist(jsonObject.getString("#text"));

                    scrobble.setTrackTitle(trackJsonObject.getString("name"));

                    jsonObject = trackJsonObject.getJSONObject("album");
                    scrobble.setAlbum(jsonObject.getString("#text"));

                    JSONArray jsonArray = trackJsonObject.getJSONArray("image");
                    String imageUri = jsonArray.getJSONObject(3).getString("#text");
                    scrobble.setImageUri(imageUri.equals("") ? null : imageUri);

                    jsonObject = trackJsonObject.getJSONObject("date");
                    scrobble.setDate(jsonObject.getLong("uts"));

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
        } catch (JSONException e) {//TODO
            e.printStackTrace();
        }

        return sessionkey;
    }

    public User parseUserInfo(String pStringToParse){

        User user = new User();

        try {
            JSONObject jsonObj = new JSONObject(pStringToParse).getJSONObject("user");

            user.setUsername(jsonObj.getString("name"));
            user.setPlaycount(jsonObj.getString("playcount"));
            user.setUrl(jsonObj.getString("url"));
            user.setRegistered(jsonObj.getJSONObject("registered").getLong("unixtime"));

            JSONArray uriArray = jsonObj.getJSONArray("image");
            user.setAvatarUri(uriArray.getJSONObject(3).getString("#text"));

        } catch (JSONException e) {//TODO
            e.printStackTrace();
        }

        return user;
    }

    public List<RankedItem> parseUserTopAlbums(String pStringToParse){

        List<RankedItem> topAlbums = new ArrayList<RankedItem>();

        try {
            JSONObject jsonObject;

            JSONObject obj = new JSONObject(pStringToParse);
            JSONArray albumsJsonArray = obj.getJSONObject("topalbums").getJSONArray("album");

            for (int i = 0; i < albumsJsonArray.length(); i++) {
                JSONObject albumJsonObject = albumsJsonArray.getJSONObject(i);

                RankedItem album = new RankedItem();

                album.setPrimaryField(albumJsonObject.getString("name"));

                album.setPlaycount(albumJsonObject.getString("playcount"));

                album.setSecondaryField(albumJsonObject.getJSONObject("artist").getString("name"));

                JSONArray jsonArray = albumJsonObject.getJSONArray("image");
                String imageUri = jsonArray.getJSONObject(3).getString("#text");
                album.setImageUri(imageUri.equals("") ? null : imageUri);

                album.setRank(albumJsonObject.getJSONObject("@attr").getString("rank"));

                topAlbums.add(album);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return topAlbums;
    }
}
