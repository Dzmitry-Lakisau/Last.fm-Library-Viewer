package by.d1makrat.library_fm;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import by.d1makrat.library_fm.image_loader.Malevich;
import by.d1makrat.library_fm.model.User;

import static by.d1makrat.library_fm.Constants.SCROBBLES_PER_PAGE_KEY;
import static by.d1makrat.library_fm.Constants.USER_KEY;

public class AppContext extends Application {

    private static final String SESSIONKEY_KEY = "session_key";
    private static final String DEFAULT_LIMIT = "10";
    private static AppContext mInstance;
    private SharedPreferences mSharedPreferences;

    private User mUser;
    private String mSessionKey;
    private String mPerPage;

    private static void initInstance(Context pContext) {
        if (mInstance == null) {
            mInstance = (AppContext) pContext;
        }
    }

    public static AppContext getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        AppContext.initInstance(this);

        Malevich.INSTANCE.setConfig(new Malevich.Config(this.getCacheDir()));

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        mUsername = mSharedPreferences.getString(USERNAME_KEY, null);
        mSessionKey = mSharedPreferences.getString(SESSIONKEY_KEY, null);
        mPerPage = mSharedPreferences.getString(SCROBBLES_PER_PAGE_KEY, DEFAULT_LIMIT);

        Gson gson = new Gson();
        String json = mSharedPreferences.getString(USER_KEY, null);
        mUser = gson.fromJson(json, User.class);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        saveSettings();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        saveSettings();
    }

    public void saveSettings(){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
//        editor.putString(USERNAME_KEY, mUsername);
        editor.putString(SESSIONKEY_KEY, mSessionKey);
        editor.putString(SCROBBLES_PER_PAGE_KEY, mPerPage);

        final Gson gson = new Gson();
        String serializedObject = gson.toJson(mUser);
        editor.putString(USER_KEY, serializedObject);
        editor.apply();
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User mUser) {
        this.mUser = mUser;
    }

    public String getSessionKey() {
        return mSessionKey;
    }

    public void setSessionKey(String pSessionKey) {
        mSessionKey = pSessionKey;
    }

    public int getLimit() {
        return Integer.valueOf(mPerPage);
    }

    public void setLimit(String pPerPage) {
        mPerPage = pPerPage;
    }
}
