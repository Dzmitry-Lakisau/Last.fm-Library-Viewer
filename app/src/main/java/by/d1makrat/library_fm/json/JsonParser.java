package by.d1makrat.library_fm.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.model.Artist;
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

        try {
            JSONObject source = new JSONObject(pStringToParse);

            JSONArray tracksJsonArray;
            if (source.has("recenttracks"))
                tracksJsonArray = source.getJSONObject("recenttracks").getJSONArray("track");
            else
                tracksJsonArray = source.getJSONObject("artisttracks").getJSONArray("track");

            for (int i = 0; i < tracksJsonArray.length(); i++) {
                JSONObject trackJsonObject = tracksJsonArray.getJSONObject(i);

                if (!trackJsonObject.has("@attr")) {//TODO ?
                    Scrobble scrobble = new Scrobble();

                    JSONObject jsonObject = trackJsonObject.getJSONObject("artist");
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
            sessionkey = (new JSONObject(pStringToParse)).getJSONObject("session").getString("key");
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
            JSONArray albumsJsonArray = (new JSONObject(pStringToParse)).getJSONObject("topalbums").getJSONArray("album");

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

    public List<RankedItem> parseUserTopArtists(String pStringToParse){

        List<RankedItem> topArtists = new ArrayList<RankedItem>();

        try {
            JSONArray artistsJsonArray = (new JSONObject(pStringToParse)).getJSONObject("topartists").getJSONArray("artist");

            for (int i = 0; i < artistsJsonArray.length(); i++) {
                JSONObject artistsJsonObject = artistsJsonArray.getJSONObject(i);

                RankedItem artist = new RankedItem();

                artist.setPrimaryField(artistsJsonObject.getString("name"));

                artist.setPlaycount(artistsJsonObject.getString("playcount"));

                JSONArray jsonArray = artistsJsonObject.getJSONArray("image");
                String imageUri = jsonArray.getJSONObject(3).getString("#text");
                artist.setImageUri(imageUri.equals("") ? null : imageUri);

                artist.setRank(artistsJsonObject.getJSONObject("@attr").getString("rank"));

                topArtists.add(artist);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return topArtists;
    }

    public List<RankedItem> parseUserTopTracks(String pStringToParse){

        List<RankedItem> topTracks = new ArrayList<RankedItem>();

        try {
            JSONArray tracksJsonArray = (new JSONObject(pStringToParse)).getJSONObject("toptracks").getJSONArray("track");

            for (int i = 0; i < tracksJsonArray.length(); i++) {
                JSONObject trackJsonObject = tracksJsonArray.getJSONObject(i);

                RankedItem track = new RankedItem();

                track.setPrimaryField(trackJsonObject.getString("name"));

                track.setPlaycount(trackJsonObject.getString("playcount"));

                track.setSecondaryField(trackJsonObject.getJSONObject("artist").getString("name"));

                JSONArray jsonArray = trackJsonObject.getJSONArray("image");
                String imageUri = jsonArray.getJSONObject(3).getString("#text");
                track.setImageUri(imageUri.equals("") ? null : imageUri);

                track.setRank(trackJsonObject.getJSONObject("@attr").getString("rank"));

                topTracks.add(track);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return topTracks;
    }

    public List<Artist> parseSearchArtistResults(String pStringToParse){
        List<Artist> artists = new ArrayList<>();

        try{
            JSONArray artistsJsonArray = (new JSONObject(pStringToParse)).getJSONObject("results").getJSONObject("artistmatches").getJSONArray("artist");

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
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return artists;
    }

    public String parseSendScrobbleResult(String pStringToParse){

        String message = null;
        try {
            JSONObject messageJsonObject = (new JSONObject(pStringToParse)).getJSONObject("scrobbles").getJSONObject("scrobble").getJSONObject("ignoredMessage");
            message = messageJsonObject.getString("code");
            if (message.equals("0")){
                message = "Accepted";
            }
            else
                message = AppContext.getInstance().getResources().getString(R.string.track_scrobble_ignored_message);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return message;
    }
}
