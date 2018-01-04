package by.d1makrat.library_fm.json.model;

import org.json.JSONObject;

public class UserInfo extends JsonModel {

    public static final String KEY_URL = "url";

    public UserInfo(JSONObject pJSON) {
        super(pJSON);
    }

    public String getUrl() {
        return mJSON.optString(KEY_URL);
    }

}
