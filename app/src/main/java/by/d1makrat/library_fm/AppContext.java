package by.d1makrat.library_fm;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import by.d1makrat.library_fm.image_loader.Malevich;
import by.d1makrat.library_fm.model.User;

import static by.d1makrat.library_fm.Constants.SCROBBLES_PER_PAGE_KEY;

public class AppContext extends Application {

    public static final String USERNAME_KEY = "username";
    public static final String SESSIONKEY_KEY = "session_key";
    private static final String DEFAULT_LIMIT = "10";
    //TODO 'm' - only for members here should be 's'
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

        //TODO just call static method
        AppContext.initInstance(this);

        Malevich.INSTANCE.setConfig(new Malevich.Config(this.getCacheDir()));

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //TODO remove commented code
//        mUsername = mSharedPreferences.getString(USERNAME_KEY, null);
        mSessionKey = mSharedPreferences.getString(SESSIONKEY_KEY, null);
        mPerPage = mSharedPreferences.getString(SCROBBLES_PER_PAGE_KEY, DEFAULT_LIMIT);

        //TODO remove warning
        Gson gson = new Gson();
        String json = mSharedPreferences.getString("user", null);
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
        //TODO remove commented code
//        editor.putString(USERNAME_KEY, mUsername);
        editor.putString(SESSIONKEY_KEY, mSessionKey);
        editor.putString(SCROBBLES_PER_PAGE_KEY, mPerPage);

        final Gson gson = new Gson();
        String serializedObject = gson.toJson(mUser);
        editor.putString("user", serializedObject);
        editor.apply();
    }

    //TODO move user management logic to separate class - single responsibility principle
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

    //TODO move to separate class
    public int getLimit() {
        return Integer.valueOf(mPerPage);
    }

    public void setLimit(String pPerPage) {
        mPerPage = pPerPage;
    }
}
