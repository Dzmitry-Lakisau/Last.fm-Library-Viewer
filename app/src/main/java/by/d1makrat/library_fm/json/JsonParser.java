package by.d1makrat.library_fm.json;

import org.json.JSONException;
import org.json.JSONObject;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.R;

import static by.d1makrat.library_fm.Constants.API_NO_ERROR;

public class JsonParser {

    private static final int MAX_IMAGE_RESOLUTION_INDEX = 4;

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

    public String parseSendScrobbleResult(String pStringToParse) throws JSONException {

        String message;

        JSONObject messageJsonObject = (new JSONObject(pStringToParse)).getJSONObject("scrobbles").getJSONObject("scrobble").getJSONObject("ignoredMessage");
        message = messageJsonObject.getString("code");
        switch (message){
            case "0":
                return AppContext.getInstance().getString(R.string.manual_fragment_scrobble_accepted);
            case "2":
                return AppContext.getInstance().getString(R.string.manual_fragment_track_ignored);
            case "3":
                return AppContext.getInstance().getString(R.string.manual_fragment_timestamp_old);
            case "4":
                return AppContext.getInstance().getString(R.string.manual_fragment_timestamp_new);
            case "5":
                return AppContext.getInstance().getString(R.string.manual_fragment_limit_exceeded);
            default:
                return AppContext.getInstance().getString(R.string.manual_fragment_ignored_message);
        }
    }
}
