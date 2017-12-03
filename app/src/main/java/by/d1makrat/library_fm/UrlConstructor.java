package by.d1makrat.library_fm;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import static by.d1makrat.library_fm.BuildConfig.SECRET;
import static by.d1makrat.library_fm.Constants.API_BASE_URL;

public class UrlConstructor {

    private Context context;
    private String sessionKey, username, limit;

    public UrlConstructor(Context pContext) {
        context = pContext;

        AppSettings appSettings = new AppSettings(context);
        sessionKey = appSettings.getSessionKey();
        username = appSettings.getUsername();
        limit = appSettings.getLimit();
    }

    public URL constructRecentScrobblesApiRequestUrl(int pPage, @Nullable String pFrom, @Nullable String pTo) throws MalformedURLException {//URL MUST be encoded. hash is not.

    TreeMap<String, String> requestParams = new TreeMap<>();

    requestParams.put("api_key", BuildConfig.API_KEY);
    requestParams.put("sk", sessionKey);
    requestParams.put("user", username);
    requestParams.put("limit", String.valueOf(limit));

    requestParams.put("method", "user.getRecentTracks");
    requestParams.put("page", String.valueOf(pPage));
    if (pFrom != null) requestParams.put("from", pFrom);
    if (pTo != null) requestParams.put("to", pTo);

    String apiSignature = generateApiSignature(requestParams);
    StringBuilder stringBuilder = new StringBuilder(API_BASE_URL);
        for (Map.Entry<String, String> e : requestParams.entrySet()) {
            stringBuilder.append(e.getKey()).append("=").append(URLEncoder.encode(e.getValue())).append("&");
    }

    stringBuilder.append("api_sig=").append(apiSignature);
    stringBuilder.append("&format=json");

    return new URL(stringBuilder.toString());
}

    public Uri constructRecentScrobblesUrlForBrowser(@Nullable String pFrom, @Nullable String pTo) {

        String urlForBrowser = "https://www.last.fm/user/" + username + "/library";

        if (pFrom != null && pTo != null) {
            java.util.Date date_from = new java.util.Date(Long.valueOf(pFrom) * 1000);
//            String string_from = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH).format(date_from);
            java.util.Date date_to = new java.util.Date(Long.valueOf(pTo) * 1000);// - TimeZone.getDefault().getRawOffset());
//            String string_to = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH).format(date_to);
//            filter_string = string_from + " - " + string_to;
            urlForBrowser += "?from=" + new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(date_from) + "&to=" + new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(date_to);
        }

        return Uri.parse(urlForBrowser);
    }

    private String generateApiSignature(Map<String, String> params) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> e : params.entrySet()) {
            stringBuilder.append(e.getKey()).append(e.getValue());
        }
        stringBuilder.append(SECRET);
        return new String(Hex.encodeHex(DigestUtils.md5(stringBuilder.toString())));
    }
}
