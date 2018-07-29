package by.d1makrat.library_fm;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import by.d1makrat.library_fm.https.AdditionalParametersInterceptor;
import by.d1makrat.library_fm.https.LastFmRestApiService;
import by.d1makrat.library_fm.image_loader.Malevich;
import by.d1makrat.library_fm.json.ScrobblesAdapter;
import by.d1makrat.library_fm.json.SearchArtistResultsAdapter;
import by.d1makrat.library_fm.json.SendScrobbleResultAdapter;
import by.d1makrat.library_fm.json.SessionKeyAdapter;
import by.d1makrat.library_fm.json.TopAlbumsAdapter;
import by.d1makrat.library_fm.json.TopArtistsAdapter;
import by.d1makrat.library_fm.json.TopTracksAdapter;
import by.d1makrat.library_fm.json.UserAdapter;
import by.d1makrat.library_fm.json.model.ArtistsJsonModel;
import by.d1makrat.library_fm.json.model.ScrobblesJsonModel;
import by.d1makrat.library_fm.model.SendScrobbleResult;
import by.d1makrat.library_fm.model.SessionKey;
import by.d1makrat.library_fm.model.TopAlbums;
import by.d1makrat.library_fm.model.TopArtists;
import by.d1makrat.library_fm.model.TopTracks;
import by.d1makrat.library_fm.model.User;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static by.d1makrat.library_fm.Constants.API_BASE_URL;
import static by.d1makrat.library_fm.Constants.SCROBBLES_PER_PAGE_KEY;
import static by.d1makrat.library_fm.Constants.USER_KEY;

public class AppContext extends Application {

    private static final String SESSIONKEY_KEY = "session_key";
    private static final String DEFAULT_LIMIT = "10";
    private static AppContext mInstance;
    private SharedPreferences mSharedPreferences;
    private LastFmRestApiService mRetrofitWebService;

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

        mRetrofitWebService = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(new OkHttpClient().newBuilder().addInterceptor(new AdditionalParametersInterceptor()).build())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .registerTypeAdapter(User.class, new UserAdapter())
                        .registerTypeAdapter(TopAlbums.class, new TopAlbumsAdapter())
                        .registerTypeAdapter(TopArtists.class, new TopArtistsAdapter())
                        .registerTypeAdapter(TopTracks.class, new TopTracksAdapter())
                        .registerTypeAdapter(ArtistsJsonModel.class, new SearchArtistResultsAdapter())
                        .registerTypeAdapter(ScrobblesJsonModel.class, new ScrobblesAdapter())
                        .registerTypeAdapter(SessionKey.class, new SessionKeyAdapter())
                        .registerTypeAdapter(SendScrobbleResult.class, new SendScrobbleResultAdapter())
                        .create()))
                .build()
                .create(LastFmRestApiService.class);

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

    public LastFmRestApiService getRetrofitWebService(){
        return mRetrofitWebService;
    }
}
