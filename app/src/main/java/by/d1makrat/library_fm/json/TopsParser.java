package by.d1makrat.library_fm.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import by.d1makrat.library_fm.model.TopAlbum;
import by.d1makrat.library_fm.model.TopArtist;
import by.d1makrat.library_fm.model.TopTrack;

import static by.d1makrat.library_fm.Constants.ALBUM_KEY;
import static by.d1makrat.library_fm.Constants.ARTIST_KEY;
import static by.d1makrat.library_fm.Constants.TRACK_KEY;

public class TopsParser {

    private String mStringToParse;

    public TopsParser(String pStringToParse) {
        mStringToParse = pStringToParse;
    }

    public List<TopAlbum> parseUserTopAlbums() throws JSONException {

        List<TopAlbum> topAlbums = new ArrayList<>();

        JSONArray albumsJsonArray = (new JSONObject(mStringToParse)).getJSONObject("topalbums").getJSONArray(ALBUM_KEY);

        for (int i = 0; i < albumsJsonArray.length(); i++) {
            JSONObject albumJsonObject = albumsJsonArray.getJSONObject(i);

            TopAlbum altopAlbumum = new TopAlbum();

            altopAlbumum.setTitle(albumJsonObject.getString("name"));

            altopAlbumum.setPlaycount(albumJsonObject.getString("playcount"));

            altopAlbumum.setArtistName(albumJsonObject.getJSONObject(ARTIST_KEY).getString("name"));

            JSONArray jsonArray = albumJsonObject.getJSONArray("image");
            String imageUri = jsonArray.getJSONObject(3).getString("#text");
            altopAlbumum.setImageUri(imageUri.equals("") ? null : imageUri);

            altopAlbumum.setRank(albumJsonObject.getJSONObject("@attr").getString("rank"));

            topAlbums.add(altopAlbumum);
        }

        return topAlbums;
    }

    public List<TopArtist> parseUserTopArtists() throws JSONException {

        List<TopArtist> topArtists = new ArrayList<>();

        JSONArray artistsJsonArray = (new JSONObject(mStringToParse)).getJSONObject("topartists").getJSONArray(ARTIST_KEY);

        for (int i = 0; i < artistsJsonArray.length(); i++) {
            JSONObject artistsJsonObject = artistsJsonArray.getJSONObject(i);

            TopArtist topArtist = new TopArtist();

            topArtist.setName(artistsJsonObject.getString("name"));

            topArtist.setPlaycount(artistsJsonObject.getString("playcount"));

            JSONArray jsonArray = artistsJsonObject.getJSONArray("image");
            String imageUri = jsonArray.getJSONObject(3).getString("#text");
            topArtist.setImageUri(imageUri.equals("") ? null : imageUri);

            topArtist.setRank(artistsJsonObject.getJSONObject("@attr").getString("rank"));

            topArtists.add(topArtist);
        }

        return topArtists;
    }

    public List<TopTrack> parseUserTopTracks() throws JSONException {

        List<TopTrack> topTracks = new ArrayList<>();

        JSONArray tracksJsonArray = (new JSONObject(mStringToParse)).getJSONObject("toptracks").getJSONArray(TRACK_KEY);

        for (int i = 0; i < tracksJsonArray.length(); i++) {
            JSONObject trackJsonObject = tracksJsonArray.getJSONObject(i);

            TopTrack topTrack = new TopTrack();

            topTrack.setTitle(trackJsonObject.getString("name"));

            topTrack.setPlaycount(trackJsonObject.getString("playcount"));

            topTrack.setArtistName(trackJsonObject.getJSONObject(ARTIST_KEY).getString("name"));

            JSONArray jsonArray = trackJsonObject.getJSONArray("image");
            String imageUri = jsonArray.getJSONObject(3).getString("#text");
            topTrack.setImageUri(imageUri.equals("") ? null : imageUri);

            topTrack.setRank(trackJsonObject.getJSONObject("@attr").getString("rank"));

            topTracks.add(topTrack);
        }

        return topTracks;
    }

    public String parseAlbumsCount() throws JSONException {
        return (new JSONObject(mStringToParse)).getJSONObject("topalbums").getJSONObject("@attr").getString("total");
    }

    public String parseArtistsCount() throws JSONException {
        return (new JSONObject(mStringToParse)).getJSONObject("topartists").getJSONObject("@attr").getString("total");
    }

    public String parseTracksCount() throws JSONException {
        return (new JSONObject(mStringToParse)).getJSONObject("toptracks").getJSONObject("@attr").getString("total");
    }
}