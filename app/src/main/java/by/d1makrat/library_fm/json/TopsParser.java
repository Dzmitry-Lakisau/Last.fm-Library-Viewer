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
import static by.d1makrat.library_fm.Constants.EMPTY_STRING;
import static by.d1makrat.library_fm.Constants.JsonConstants.ATTRIBUTE_KEY;
import static by.d1makrat.library_fm.Constants.JsonConstants.IMAGE_KEY;
import static by.d1makrat.library_fm.Constants.JsonConstants.TEXT_KEY;
import static by.d1makrat.library_fm.Constants.NAME_KEY;
import static by.d1makrat.library_fm.Constants.PLAYCOUNT_KEY;
import static by.d1makrat.library_fm.Constants.RANK_KEY;
import static by.d1makrat.library_fm.Constants.TRACK_KEY;

public class TopsParser {

    private static final String TOPALBUMS_KEY = "topalbums";
    private static final String TOPARTISTS_KEY = "topartists";
    private static final String TOPTRACKS_KEY = "toptracks";
    private static final int MAX_IMAGE_RESOLUTION_INDEX = 3;
    private static final String TOTAL_KEY = "total";
    private String mStringToParse;

    public TopsParser(String pStringToParse) {
        mStringToParse = pStringToParse;
    }

    public List<TopAlbum> parseUserTopAlbums() throws JSONException {

        List<TopAlbum> topAlbums = new ArrayList<>();

        JSONArray albumsJsonArray = (new JSONObject(mStringToParse)).getJSONObject(TOPALBUMS_KEY).getJSONArray(ALBUM_KEY);

        for (int i = 0; i < albumsJsonArray.length(); i++) {
            JSONObject albumJsonObject = albumsJsonArray.getJSONObject(i);

            TopAlbum altopAlbumum = new TopAlbum();

            altopAlbumum.setTitle(albumJsonObject.getString(NAME_KEY));

            altopAlbumum.setPlaycount(albumJsonObject.getString(PLAYCOUNT_KEY));

            altopAlbumum.setArtistName(albumJsonObject.getJSONObject(ARTIST_KEY).getString(NAME_KEY));

            JSONArray jsonArray = albumJsonObject.getJSONArray(IMAGE_KEY);
            String imageUri = jsonArray.getJSONObject(MAX_IMAGE_RESOLUTION_INDEX).getString(TEXT_KEY);
            altopAlbumum.setImageUri(imageUri.equals(EMPTY_STRING) ? null : imageUri);

            altopAlbumum.setRank(albumJsonObject.getJSONObject(ATTRIBUTE_KEY).getString(RANK_KEY));

            topAlbums.add(altopAlbumum);
        }

        return topAlbums;
    }

    public List<TopArtist> parseUserTopArtists() throws JSONException {

        List<TopArtist> topArtists = new ArrayList<>();

        JSONArray artistsJsonArray = (new JSONObject(mStringToParse)).getJSONObject(TOPARTISTS_KEY).getJSONArray(ARTIST_KEY);

        for (int i = 0; i < artistsJsonArray.length(); i++) {
            JSONObject artistsJsonObject = artistsJsonArray.getJSONObject(i);

            TopArtist topArtist = new TopArtist();

            topArtist.setName(artistsJsonObject.getString(NAME_KEY));

            topArtist.setPlaycount(artistsJsonObject.getString(PLAYCOUNT_KEY));

            JSONArray jsonArray = artistsJsonObject.getJSONArray(IMAGE_KEY);
            String imageUri = jsonArray.getJSONObject(MAX_IMAGE_RESOLUTION_INDEX).getString(TEXT_KEY);
            topArtist.setImageUri(imageUri.equals(EMPTY_STRING) ? null : imageUri);

            topArtist.setRank(artistsJsonObject.getJSONObject(ATTRIBUTE_KEY).getString(RANK_KEY));

            topArtists.add(topArtist);
        }

        return topArtists;
    }

    public List<TopTrack> parseUserTopTracks() throws JSONException {

        List<TopTrack> topTracks = new ArrayList<>();

        JSONArray tracksJsonArray = (new JSONObject(mStringToParse)).getJSONObject(TOPTRACKS_KEY).getJSONArray(TRACK_KEY);

        for (int i = 0; i < tracksJsonArray.length(); i++) {
            JSONObject trackJsonObject = tracksJsonArray.getJSONObject(i);

            TopTrack topTrack = new TopTrack();

            topTrack.setTitle(trackJsonObject.getString(NAME_KEY));

            topTrack.setPlaycount(trackJsonObject.getString(PLAYCOUNT_KEY));

            topTrack.setArtistName(trackJsonObject.getJSONObject(ARTIST_KEY).getString(NAME_KEY));

            JSONArray jsonArray = trackJsonObject.getJSONArray(IMAGE_KEY);
            String imageUri = jsonArray.getJSONObject(MAX_IMAGE_RESOLUTION_INDEX).getString(TEXT_KEY);
            topTrack.setImageUri(imageUri.equals(EMPTY_STRING) ? null : imageUri);

            topTrack.setRank(trackJsonObject.getJSONObject(ATTRIBUTE_KEY).getString(RANK_KEY));

            topTracks.add(topTrack);
        }

        return topTracks;
    }

    public String parseAlbumsCount() throws JSONException {
        return (new JSONObject(mStringToParse)).getJSONObject(TOPALBUMS_KEY).getJSONObject(ATTRIBUTE_KEY).getString(TOTAL_KEY);
    }

    public String parseArtistsCount() throws JSONException {
        return (new JSONObject(mStringToParse)).getJSONObject(TOPARTISTS_KEY).getJSONObject(ATTRIBUTE_KEY).getString(TOTAL_KEY);
    }

    public String parseTracksCount() throws JSONException {
        return (new JSONObject(mStringToParse)).getJSONObject(TOPTRACKS_KEY).getJSONObject(ATTRIBUTE_KEY).getString(TOTAL_KEY);
    }
}