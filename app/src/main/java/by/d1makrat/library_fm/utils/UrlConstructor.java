package by.d1makrat.library_fm.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.BuildConfig;

import static by.d1makrat.library_fm.BuildConfig.SECRET;
import static by.d1makrat.library_fm.Constants.ALBUM_KEY;
import static by.d1makrat.library_fm.Constants.ARTIST_KEY;
import static by.d1makrat.library_fm.Constants.DATE_LONG_DEFAUT_VALUE;
import static by.d1makrat.library_fm.Constants.PERIOD_KEY;
import static by.d1makrat.library_fm.Constants.TRACK_KEY;
import static by.d1makrat.library_fm.Constants.USER_KEY;

public class UrlConstructor {

    private static final String API_BASE_URL = "https://ws.audioscrobbler.com/2.0/?";
    private static final String APIKEY_KEY = "api_key";
    private static final String SESSIONKEY_KEY = "sk";
    private static final String SCROBBLES_PER_PAGE_KEY = "limit";
    private static final String METHOD_KEY = "method";
    private static final String METHOD_GET_RECENT_TRACKS_VALUE = "user.getRecentTracks";
    private static final String PAGE_KEY = "page";
    private static final String FROM_KEY = "from";
    private static final String TO_KEY = "to";
    private static final String API_SIGNATURE_KEY = "api_sig=";
    private static final String FORMAT_KEY = "format=json";
    private static final String METHOD_GET_ARTIST_TRACKS_VALUE = "user.getArtistTracks";
    private static final String START_TIMESTAMP_KEY = "startTimestamp";
    private static final String END_TIMESTAMP_KEY = "endTimestamp";
    private static final String METHOD_GET_MOBILE_SESSION_VALUE = "auth.getMobileSession";
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";
    private static final String METHOD_GET_USER_INFO = "user.getInfo";
    private static final String METHOD_GET_USER_TOP_ALBUMS_VALUE = "user.getTopAlbums";
    private static final String METHOD_GET_USER_TOP_ARTISTS_VALUE = "user.getTopArtists";
    private static final String METHOD_GET_USER_TOP_TRACKS_VALUE = "user.getTopTracks";
    private static final String METHOD_SEARCH_ARTISTS_VALUE = "artist.search";
    private static final String TRACK_NUMBER_KEY = "trackNumber";
    private static final String TRACK_DURATION_KEY = "duration";
    private static final String TIMESTAMP_KEY = "timestamp";
    private static final String METHOD_SCROBBLE_TRACK_VALUE = "track.scrobble";

    private String mSessionKey, mUsername, mPerPage;

    public UrlConstructor() {
        mSessionKey = AppContext.getInstance().getSessionKey();
        mUsername = AppContext.getInstance().getUser() != null ? AppContext.getInstance().getUser().getUsername() : null ;
        mPerPage = String.valueOf(AppContext.getInstance().getLimit());
    }

    @NonNull
    private String generateApiSignature(TreeMap<String, String> params) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> e : params.entrySet()) {
            stringBuilder.append(e.getKey()).append(e.getValue());
        }
        stringBuilder.append(SECRET);
        return new String(Hex.encodeHex(DigestUtils.md5(stringBuilder.toString())));
    }

    private URL appendParams(Map<String, String> pParams) throws MalformedURLException{
        String apiSignature = generateApiSignature(new TreeMap<>(pParams));
        StringBuilder stringBuilder = new StringBuilder(API_BASE_URL);
        for (Map.Entry<String, String> e : pParams.entrySet()) {
            stringBuilder.append(e.getKey()).append("=").append(URLEncoder.encode(e.getValue())).append("&");
        }
        stringBuilder.append(API_SIGNATURE_KEY).append(apiSignature).append("&");
        stringBuilder.append(FORMAT_KEY);

        return new URL(stringBuilder.toString());
    }

    public URL constructRecentScrobblesApiRequestUrl(int pPage, Long pFrom, Long pTo) throws MalformedURLException {//URL MUST be encoded. hash is not.

        Map<String, String> requestParams = new TreeMap<>();

        requestParams.put(APIKEY_KEY, BuildConfig.API_KEY);
        requestParams.put(SESSIONKEY_KEY, mSessionKey);
        requestParams.put(USER_KEY, mUsername);
        requestParams.put(SCROBBLES_PER_PAGE_KEY, mPerPage);
        requestParams.put(METHOD_KEY, METHOD_GET_RECENT_TRACKS_VALUE);
        requestParams.put(PAGE_KEY, String.valueOf(pPage));
        if (!pFrom.equals(DATE_LONG_DEFAUT_VALUE)) requestParams.put(FROM_KEY, String.valueOf(pFrom));
        if (!pTo.equals(DATE_LONG_DEFAUT_VALUE)) requestParams.put(TO_KEY, String.valueOf(pTo));

        return appendParams(requestParams);
    }

    public URL constructScrobblesByArtistApiRequestUrl(String pArtist, int pPage, Long pFrom, Long pTo) throws MalformedURLException {

        Map<String, String> requestParams = new TreeMap<>();

        requestParams.put(APIKEY_KEY, BuildConfig.API_KEY);
        requestParams.put(SESSIONKEY_KEY, mSessionKey);
        requestParams.put(USER_KEY, mUsername);
        requestParams.put(SCROBBLES_PER_PAGE_KEY, mPerPage);
        requestParams.put(METHOD_KEY, METHOD_GET_ARTIST_TRACKS_VALUE);
        requestParams.put(ARTIST_KEY, pArtist);
        requestParams.put(PAGE_KEY, String.valueOf(pPage));
        if (!pFrom.equals(DATE_LONG_DEFAUT_VALUE)) requestParams.put(START_TIMESTAMP_KEY, String.valueOf(pFrom));
        if (!pTo.equals(DATE_LONG_DEFAUT_VALUE)) requestParams.put(END_TIMESTAMP_KEY, String.valueOf(pTo));

        return appendParams(requestParams);
    }

    public URL constructGetSessionKeyApiRequestUrl(String pUsername, String pPassword) throws MalformedURLException {

        Map<String, String> requestParams = new LinkedHashMap<>();

        requestParams.put(METHOD_KEY, METHOD_GET_MOBILE_SESSION_VALUE);
        requestParams.put(APIKEY_KEY, BuildConfig.API_KEY);
        requestParams.put(USERNAME_KEY, pUsername);
        requestParams.put(PASSWORD_KEY, pPassword);

        return appendParams(requestParams);
    }

    public URL constructGetUserInfoApiRequestUrl(@Nullable String pUsername) throws MalformedURLException {

        Map<String, String> requestParams = new TreeMap<>();

        requestParams.put(APIKEY_KEY, BuildConfig.API_KEY);
        requestParams.put(METHOD_KEY, METHOD_GET_USER_INFO);
        requestParams.put(SESSIONKEY_KEY, mSessionKey);
        if (pUsername != null) requestParams.put(USER_KEY, pUsername);

        return appendParams(requestParams);
    }

    public URL constructGetUserTopAlbumsApiRequestUrl(@NonNull String pPeriod, int pPage) throws MalformedURLException {

        Map<String, String> requestParams = new TreeMap<>();

        requestParams.put(APIKEY_KEY, BuildConfig.API_KEY);
        requestParams.put(USER_KEY, mUsername);
        requestParams.put(SCROBBLES_PER_PAGE_KEY, mPerPage);
        requestParams.put(METHOD_KEY, METHOD_GET_USER_TOP_ALBUMS_VALUE);
        requestParams.put(PAGE_KEY, String.valueOf(pPage));
        requestParams.put(PERIOD_KEY, pPeriod);

        return appendParams(requestParams);
    }

    public URL constructGetUserTopArtistsApiRequestUrl(@NonNull String pPeriod, int pPage) throws MalformedURLException {

        Map<String, String> requestParams = new TreeMap<>();

        requestParams.put(APIKEY_KEY, BuildConfig.API_KEY);
        requestParams.put(USER_KEY, mUsername);
        requestParams.put(SCROBBLES_PER_PAGE_KEY, mPerPage);
        requestParams.put(METHOD_KEY, METHOD_GET_USER_TOP_ARTISTS_VALUE);
        requestParams.put(PAGE_KEY, String.valueOf(pPage));
        requestParams.put(PERIOD_KEY, pPeriod);

        return appendParams(requestParams);
    }

    public URL constructGetUserTopTracksApiRequestUrl(@NonNull String pPeriod, int pPage) throws MalformedURLException {

        Map<String, String> requestParams = new TreeMap<>();

        requestParams.put(APIKEY_KEY, BuildConfig.API_KEY);
        requestParams.put(USER_KEY, mUsername);
        requestParams.put(SCROBBLES_PER_PAGE_KEY, mPerPage);
        requestParams.put(METHOD_KEY, METHOD_GET_USER_TOP_TRACKS_VALUE);
        requestParams.put(PAGE_KEY, String.valueOf(pPage));
        requestParams.put(PERIOD_KEY, pPeriod);

        return appendParams(requestParams);
    }

    public URL constructSearchArtistsApiRequestUrl(@NonNull String pSearchQuery, int pPage) throws MalformedURLException {

        Map<String, String> requestParams = new TreeMap<>();

        requestParams.put(APIKEY_KEY, BuildConfig.API_KEY);
        requestParams.put(ARTIST_KEY, pSearchQuery);
        requestParams.put(SCROBBLES_PER_PAGE_KEY, mPerPage);
        requestParams.put(METHOD_KEY, METHOD_SEARCH_ARTISTS_VALUE);
        requestParams.put(PAGE_KEY, String.valueOf(pPage));

        return appendParams(requestParams);
    }

    public URL constructSendScrobbleApiRequestUrl(@NonNull String pTrack, @NonNull String pArtist, String pAlbum, String pTrackNumber,
                                                  @NonNull String pTrackDuration, @NonNull String pTimestamp) throws MalformedURLException {

        Map<String, String> requestParams = new TreeMap<>();

        requestParams.put(APIKEY_KEY, BuildConfig.API_KEY);
        requestParams.put(SESSIONKEY_KEY, mSessionKey);
        requestParams.put(ARTIST_KEY, pArtist);
        requestParams.put(TRACK_KEY, pTrack);
        requestParams.put(ALBUM_KEY, pAlbum);
        requestParams.put(TRACK_NUMBER_KEY, pTrackNumber);
        requestParams.put(TRACK_DURATION_KEY, pTrackDuration);
        requestParams.put(TIMESTAMP_KEY, pTimestamp);
        requestParams.put(METHOD_KEY, METHOD_SCROBBLE_TRACK_VALUE);

        return appendParams(requestParams);
    }
}
