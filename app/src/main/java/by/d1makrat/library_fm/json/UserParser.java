package by.d1makrat.library_fm.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import by.d1makrat.library_fm.model.User;

public class UserParser {

    private String mStringToParse;

    public UserParser(String pStringToParse) {
        mStringToParse = pStringToParse;
    }

    public User parse() throws JSONException {

        User user = new User();

        JSONObject jsonObj = new JSONObject(mStringToParse).getJSONObject("user");

        user.setUsername(jsonObj.getString("name"));
        user.setPlaycount(jsonObj.getString("playcount"));
        user.setUrl(jsonObj.getString("url"));
        user.setRegistered(jsonObj.getJSONObject("registered").getString("unixtime"));

        JSONArray uriArray = jsonObj.getJSONArray("image");
        user.setAvatarUri(uriArray.getJSONObject(3).getString("#text"));

        return user;
    }
}
