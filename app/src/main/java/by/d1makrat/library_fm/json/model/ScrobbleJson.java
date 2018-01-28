package by.d1makrat.library_fm.json.model;

import org.json.JSONException;
import org.json.JSONObject;

import static by.d1makrat.library_fm.Constants.ALBUM_KEY;
import static by.d1makrat.library_fm.Constants.ARTIST_KEY;
import static by.d1makrat.library_fm.Constants.DATE_KEY;
import static by.d1makrat.library_fm.Constants.JsonConstants.IMAGE_KEY;
import static by.d1makrat.library_fm.Constants.JsonConstants.TEXT_KEY;
import static by.d1makrat.library_fm.Constants.NAME_KEY;

public class ScrobbleJson {

    private static final int MAX_IMAGE_RESOLUTION_INDEX = 3;
    private JSONObject mRootJsonObject;

    public ScrobbleJson(JSONObject pRootJsonObject) throws JSONException {
        mRootJsonObject = pRootJsonObject;
    }

    public String getArtist() throws JSONException {
        return mRootJsonObject.getJSONObject(ARTIST_KEY).getString(TEXT_KEY);
    }

    public String getTrack() throws JSONException {
        return mRootJsonObject.getString(NAME_KEY);
    }

    public String getAlbum() throws JSONException {
        return mRootJsonObject.getJSONObject(ALBUM_KEY).getString(TEXT_KEY);
    }

    public String getImageUrl() throws JSONException {
        return mRootJsonObject.getJSONArray(IMAGE_KEY).getJSONObject(MAX_IMAGE_RESOLUTION_INDEX).getString(TEXT_KEY);
    }

    public long getDate() throws JSONException {
        return mRootJsonObject.getJSONObject(DATE_KEY).getLong("uts");
    }
}