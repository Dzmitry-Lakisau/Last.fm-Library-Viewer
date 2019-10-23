package by.d1makrat.library_fm;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.List;

import by.d1makrat.library_fm.database.DatabaseHelper;
import by.d1makrat.library_fm.image_loader.Malevich;
import by.d1makrat.library_fm.json.ScrobblesAdapter;
import by.d1makrat.library_fm.json.SearchArtistResultsAdapter;
import by.d1makrat.library_fm.json.SendScrobbleResultAdapter;
import by.d1makrat.library_fm.json.SessionKeyAdapter;
import by.d1makrat.library_fm.json.TopAlbumsAdapter;
import by.d1makrat.library_fm.json.TopArtistsAdapter;
import by.d1makrat.library_fm.json.TopTracksAdapter;
import by.d1makrat.library_fm.json.TracksAdapter;
import by.d1makrat.library_fm.json.UserAdapter;
import by.d1makrat.library_fm.json.UserSharedPreferencesAdapter;
import by.d1makrat.library_fm.json.model.ArtistsJsonModel;
import by.d1makrat.library_fm.json.model.ScrobblesJsonModel;
import by.d1makrat.library_fm.model.SendScrobbleResult;
import by.d1makrat.library_fm.model.SessionKey;
import by.d1makrat.library_fm.model.TopAlbums;
import by.d1makrat.library_fm.model.TopArtists;
import by.d1makrat.library_fm.model.TopTracks;
import by.d1makrat.library_fm.model.Track;
import by.d1makrat.library_fm.model.User;
import by.d1makrat.library_fm.repository.Repository;
import by.d1makrat.library_fm.retrofit.AdditionalParametersInterceptor;
import by.d1makrat.library_fm.retrofit.LastFmRestApiService;
import io.fabric.sdk.android.Fabric;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static by.d1makrat.library_fm.Constants.API_BASE_URL;
import static by.d1makrat.library_fm.Constants.USER_KEY;

public class AppContext extends MultiDexApplication {

    private static final String SCROBBLES_PER_PAGE_KEY = "scrobbles_per_page";
    private static final String SESSIONKEY_KEY = "session_key";
    private static final String DEFAULT_LIMIT = "10";

    private static AppContext sInstance;
    private SharedPreferences mSharedPreferences;
    private LastFmRestApiService mRetrofitWebService;
    public Repository repository;

    private User mUser;
    private String mSessionKey;
    private String mPerPage;

    private static void initInstance(Context pContext) {
        if (sInstance == null) {
            sInstance = (AppContext) pContext;
        }
    }

    public static AppContext getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        AppContext.initInstance(this);

        Fabric.with(this,
                new Crashlytics.Builder()
                        .core(new CrashlyticsCore.Builder()
                                .disabled(BuildConfig.DEBUG)
                                .build())
                        .build()
        );

        Malevich.INSTANCE.setConfig(new Malevich.Config(this.getCacheDir()));

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient().newBuilder().addInterceptor(new AdditionalParametersInterceptor());

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);
        }

        mRetrofitWebService = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(okHttpClientBuilder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .registerTypeAdapter(User.class, new UserAdapter())
                        .registerTypeAdapter(TopAlbums.class, new TopAlbumsAdapter())
                        .registerTypeAdapter(TopArtists.class, new TopArtistsAdapter())
                        .registerTypeAdapter(TopTracks.class, new TopTracksAdapter())
                        .registerTypeAdapter(ArtistsJsonModel.class, new SearchArtistResultsAdapter())
                        .registerTypeAdapter(ScrobblesJsonModel.class, new ScrobblesAdapter())
                        .registerTypeAdapter(SessionKey.class, new SessionKeyAdapter())
                        .registerTypeAdapter(SendScrobbleResult.class, new SendScrobbleResultAdapter())
                        .registerTypeAdapter(new TypeToken<List<Track>>() {}.getType(), new TracksAdapter())
                        .create()))
                .build()
                .create(LastFmRestApiService.class);

        repository = new Repository(mRetrofitWebService, new DatabaseHelper(AppContext.getInstance()));

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mSessionKey = mSharedPreferences.getString(SESSIONKEY_KEY, null);
        mPerPage = mSharedPreferences.getString(SCROBBLES_PER_PAGE_KEY, DEFAULT_LIMIT);
        String userSharedPreferences = mSharedPreferences.getString(USER_KEY, null);

        mUser = new GsonBuilder().registerTypeAdapter(User.class, new UserSharedPreferencesAdapter().nullSafe())
                .create().fromJson(userSharedPreferences, User.class);
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
