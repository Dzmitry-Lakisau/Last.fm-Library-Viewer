package by.d1makrat.library_fm.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import by.d1makrat.library_fm.model.User;

import static by.d1makrat.library_fm.Constants.JsonConstants.IMAGE_KEY;
import static by.d1makrat.library_fm.Constants.JsonConstants.TEXT_KEY;
import static by.d1makrat.library_fm.Constants.NAME_KEY;
import static by.d1makrat.library_fm.Constants.PLAYCOUNT_KEY;
import static by.d1makrat.library_fm.Constants.URL_KEY;
import static by.d1makrat.library_fm.Constants.USER_KEY;

public class UserParser {

    private static final String REGISTERED_KEY = "registered";
    private static final String UNIXTIME_KEY = "unixtime";
    private static final int MAX_IMAGE_RESOLUTION_INDEX = 3;
    private final String mStringToParse;

    public UserParser(String pStringToParse) {
        mStringToParse = pStringToParse;
    }

    public User parse() throws JSONException {

        User user = new User();

        JSONObject jsonObj = new JSONObject(mStringToParse).getJSONObject(USER_KEY);

        user.setUsername(jsonObj.getString(NAME_KEY));
        user.setPlaycount(jsonObj.getString(PLAYCOUNT_KEY));
        user.setUrl(jsonObj.getString(URL_KEY));
        user.setRegistered(jsonObj.getJSONObject(REGISTERED_KEY).getString(UNIXTIME_KEY));

        JSONArray uriArray = jsonObj.getJSONArray(IMAGE_KEY);
        user.setAvatarUri(uriArray.getJSONObject(MAX_IMAGE_RESOLUTION_INDEX).getString(TEXT_KEY));

        return user;
    }
}
