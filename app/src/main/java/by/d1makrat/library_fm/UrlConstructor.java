package by.d1makrat.library_fm;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

import static by.d1makrat.library_fm.BuildConfig.SECRET;

public class UrlConstructor {

    private static final String API_BASE_URL = "https://ws.audioscrobbler.com/2.0/?";
    private static final String APIKEY_KEY = "api_key";
    private static final String SESSIONKEY_KEY = "sk";
    private static final String USERNAME_KEY = "user";
    private static final String SCROBBLES_PER_PAGE_KEY = "limit";
    private static final String METHOD_KEY = "method";
    private static final String METHOD_GET_RECENT_TRACKS_VALUE = "user.getRecentTracks";
    private static final String PAGE_KEY = "page";
    private static final String FROM_KEY = "from";
    private static final String TO_KEY = "to";
    private static final String API_SIGNATURE_KEY = "api_sig=";
    private static final String FORMAT_KEY = "&format=json";
    private static final String METHOD_GET_ARTIST_TRACKS_VALUE = "user.getArtistTracks";
    private static final String ARTIST_KEY = "artist";
    private static final String START_TIMESTAMP_KEY = "startTimestamp";
    private static final String END_TIMESTAMP_KEY = "endTimestamp";

    private String mSessionKey, mUsername, mPerPage;

    public UrlConstructor() {
        mSessionKey = AppContext.getInstance().getSessionKey();
        mUsername = AppContext.getInstance().getUsername();
        mPerPage = AppContext.getInstance().getLimit();
    }

    public URL constructRecentScrobblesApiRequestUrl(String pPage, @Nullable String pFrom, @Nullable String pTo) throws MalformedURLException {//URL MUST be encoded. hash is not.

        TreeMap<String, String> requestParams = new TreeMap<>();

        requestParams.put(APIKEY_KEY, BuildConfig.API_KEY);
        requestParams.put(SESSIONKEY_KEY, mSessionKey);
        requestParams.put(USERNAME_KEY, mUsername);
        requestParams.put(SCROBBLES_PER_PAGE_KEY, mPerPage);

        requestParams.put(METHOD_KEY, METHOD_GET_RECENT_TRACKS_VALUE);
        requestParams.put(PAGE_KEY, pPage);
        if (pFrom != null) requestParams.put(FROM_KEY, pFrom);
        if (pTo != null) requestParams.put(TO_KEY, pTo);

        String apiSignature = generateApiSignature(requestParams);
        StringBuilder stringBuilder = new StringBuilder(API_BASE_URL);
        for (Map.Entry<String, String> e : requestParams.entrySet()) {
            stringBuilder.append(e.getKey()).append("=").append(URLEncoder.encode(e.getValue())).append("&");
        }

        stringBuilder.append(API_SIGNATURE_KEY).append(apiSignature);
        stringBuilder.append(FORMAT_KEY);

        return new URL(stringBuilder.toString());
    }

    @NonNull
    private String generateApiSignature(Map<String, String> params) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> e : params.entrySet()) {
            stringBuilder.append(e.getKey()).append(e.getValue());
        }
        stringBuilder.append(SECRET);
        return new String(Hex.encodeHex(DigestUtils.md5(stringBuilder.toString())));
    }

    public URL constructScrobblesByArtistApiRequestUrl(String pArtist, String pPage, String pFrom, String pTo) throws MalformedURLException {

        TreeMap<String, String> requestParams = new TreeMap<>();

        requestParams.put(APIKEY_KEY, BuildConfig.API_KEY);
        requestParams.put(SESSIONKEY_KEY, mSessionKey);
        requestParams.put(USERNAME_KEY, mUsername);
        requestParams.put(SCROBBLES_PER_PAGE_KEY, mPerPage);

        requestParams.put(METHOD_KEY, METHOD_GET_ARTIST_TRACKS_VALUE);
        requestParams.put(ARTIST_KEY, pArtist);
        requestParams.put(PAGE_KEY, pPage);
        if (pFrom != null) requestParams.put(START_TIMESTAMP_KEY, pFrom);
        if (pTo != null) requestParams.put(END_TIMESTAMP_KEY, pTo);

        String apiSignature = generateApiSignature(requestParams);
        StringBuilder stringBuilder = new StringBuilder(API_BASE_URL);
        for (Map.Entry<String, String> e : requestParams.entrySet()) {
            stringBuilder.append(e.getKey()).append("=").append(URLEncoder.encode(e.getValue())).append("&");
        }

        stringBuilder.append(API_SIGNATURE_KEY).append(apiSignature);
        stringBuilder.append(FORMAT_KEY);

        return new URL(stringBuilder.toString());
    }

//    public URL constructScrobblesByTrackApiRequestUrl(String pArtist, String pPage, String pFrom, String pTo) throws MalformedURLException {
//
//        TreeMap<String, String> requestParams = new TreeMap<>();
//
//        requestParams.put(APIKEY_KEY, BuildConfig.API_KEY);
//        requestParams.put(SESSIONKEY_KEY, mSessionKey);
//        requestParams.put(USERNAME_KEY, mUsername);
//        requestParams.put(SCROBBLES_PER_PAGE_KEY, mPerPage);
//
//        requestParams.put(METHOD_KEY, METHOD_GET_ARTIST_TRACKS_VALUE);
//        requestParams.put(ARTIST_KEY, pArtist);
//        requestParams.put(SCROBBLES_PER_PAGE_KEY, pPage);
//        if (pFrom != null) requestParams.put(START_TIMESTAMP_KEY, pFrom);
//        if (pTo != null) requestParams.put(END_TIMESTAMP_KEY, pTo);
//
//        String apiSignature = generateApiSignature(requestParams);
//        StringBuilder stringBuilder = new StringBuilder(API_BASE_URL);
//        for (Map.Entry<String, String> e : requestParams.entrySet()) {
//            stringBuilder.append(e.getKey()).append("=").append(URLEncoder.encode(e.getValue())).append("&");
//        }
//
//        stringBuilder.append(API_SIGNATURE_KEY + "=").append(apiSignature);
//        stringBuilder.append(FORMAT_KEY);
//
//        return new URL(stringBuilder.toString());
//    }
}
