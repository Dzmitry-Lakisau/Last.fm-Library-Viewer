package by.d1makrat.library_fm.json.model;

import org.json.JSONException;
import org.json.JSONObject;

import by.d1makrat.library_fm.model.IUser;
import by.d1makrat.library_fm.model.User;

public class UserInfoJson extends User implements IUser {

    public static final String KEY_URL = "url";
    private JSONObject mJSON;

    public UserInfoJson(JSONObject pJSON) throws JSONException {
//        super(pJSON);
        mJSON = pJSON.getJSONObject("user");
    }

    @Override
    public String getUsername(){
        return mJSON.optString("name");
    }

    @Override
    public String getPlaycount(){
        return mJSON.optString("playcount");
    }

    @Override
    public String getRegistered(){
        return mJSON.optJSONObject("registered").optString("unixtime");
    }

    @Override
    public String getUrl() {
        return mJSON.optString(KEY_URL);
    }

    @Override
    public String getAvatarUri(){
        return mJSON.optJSONArray("image").optJSONObject(3).optString("#text");
    }
}
