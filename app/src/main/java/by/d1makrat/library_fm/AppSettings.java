package by.d1makrat.library_fm;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.File;

public class AppSettings {

    private static final String DEFAULT_LIMIT = "10";
    private static final String DEFAULT_RESOLUTION = "medium";

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private String mPathToBlankAlbumart;
    private String mPathToBlankArtist;

    public AppSettings(Context pContext){
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(pContext);
        mContext = pContext;
//        mResolution = "medium";
//        mLimit = "10";
    }

    public String getPathToBlankAlbumart(){
        return mContext.getFilesDir().getPath() +  File.separator + "blank_albumart.png";
    }

    public void setPathToBlankAlbumart(String pPathToBlankAlbumart) {
        mPathToBlankAlbumart = pPathToBlankAlbumart;
    }

    public String getPathToBlankArtist() {
        return mContext.getFilesDir().getPath() +  File.separator + "ic_person.png";
    }

    public void setPathToBlankArtist(String pPathToBlankArtist) {
        mPathToBlankArtist = pPathToBlankArtist;
    }

    public String getCacheDir() {
        return mSharedPreferences.getString("pathToCache", null);
    }

    public void setCacheDir(String pCacheDir) {
        SharedPreferences.Editor spEditor = mSharedPreferences.edit();
        spEditor.putString("pathToCache", pCacheDir);
        spEditor.apply();
    }

    public String getResolution() {
        return mSharedPreferences.getString("resolution", DEFAULT_RESOLUTION);
    }

    public void setResolution(String pResolution) {
        SharedPreferences.Editor spEditor = mSharedPreferences.edit();
        spEditor.putString("limit", pResolution);
        spEditor.apply();
    }

    public String getLimit() {
        return mSharedPreferences.getString("limit", DEFAULT_LIMIT);
    }

    public void setLimit(String pLimit) {
        SharedPreferences.Editor spEditor = mSharedPreferences.edit();
        spEditor.putString("limit", pLimit);
        spEditor.apply();
    }

    public String getUsername(){
        return mSharedPreferences.getString("username", null);
    }

    public void setUsername(String pUsername){
        SharedPreferences.Editor spEditor = mSharedPreferences.edit();
        spEditor.putString("username", pUsername);
        spEditor.apply();
    }

    public String getSessionKey(){
        return mSharedPreferences.getString("sessionKey", null);
    }

    public void setSessionKey(String pSessionKey){
        SharedPreferences.Editor spEditor = mSharedPreferences.edit();
        spEditor.putString("sessionKey", pSessionKey);
        spEditor.apply();
    }

    public String getRegistered(){
        return mSharedPreferences.getString("registered", null);
    }

    public void setRegistered(String pRegistered){
        SharedPreferences.Editor spEditor = mSharedPreferences.edit();
        spEditor.putString("registered", pRegistered);
        spEditor.apply();
    }

    public String getPlayccunt(){
        return mSharedPreferences.getString("playcount", null);
    }

    public void setpPlaycount(String pPlaycount){
        SharedPreferences.Editor spEditor = mSharedPreferences.edit();
        spEditor.putString("playcount", pPlaycount);
        spEditor.apply();
    }
}
