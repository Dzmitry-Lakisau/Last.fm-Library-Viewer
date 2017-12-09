package by.d1makrat.library_fm;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import by.d1makrat.library_fm.image_loader.Malevich;

import static by.d1makrat.library_fm.Constants.SCROBBLES_PER_PAGE_KEY;

public class AppContext extends Application {

    public static final String USERNAME_KEY = "username";
    public static final String SESSIONKEY_KEY = "session_key";
    private static final String DEFAULT_LIMIT = "10";
    private static AppContext mInstance;
    private SharedPreferences mSharedPreferences;
    private String mUsername;
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
        mUsername = mSharedPreferences.getString(USERNAME_KEY, null);
        mSessionKey = mSharedPreferences.getString(SESSIONKEY_KEY, null);
        mPerPage = mSharedPreferences.getString(SCROBBLES_PER_PAGE_KEY, DEFAULT_LIMIT);
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
        editor.putString(USERNAME_KEY, mUsername);
        editor.putString(SESSIONKEY_KEY, mSessionKey);
        editor.putString(SCROBBLES_PER_PAGE_KEY, mPerPage);
        editor.apply();
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String pUsername) {
        mUsername = pUsername;
    }

    public String getSessionKey() {
        return mSessionKey;
    }

    public void setSessionKey(String pSessionKey) {
        mSessionKey = pSessionKey;
    }

    public String getLimit() {
        return mPerPage;
    }

    public void setLimit(String pPerPage) {
        mPerPage = pPerPage;
    }
}
