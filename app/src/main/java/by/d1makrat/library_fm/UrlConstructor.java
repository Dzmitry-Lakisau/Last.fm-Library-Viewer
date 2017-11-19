package by.d1makrat.library_fm;

import android.content.Context;
import android.support.annotation.Nullable;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

import static by.d1makrat.library_fm.BuildConfig.SECRET;

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

    public URL constructRecentScrobblesRequest(int pPage, @Nullable String pFrom, @Nullable String pTo) throws MalformedURLException {//URL MUST be encoded. hash is not.

    TreeMap<String, String> requestParams = new TreeMap<>();

    requestParams.put("api_key", BuildConfig.API_KEY);
    requestParams.put("sk", sessionKey);
    requestParams.put("user", username);
    requestParams.put("limit", String.valueOf(limit));

    requestParams.put("method", "user.getRecentTracks");
    requestParams.put("page", String.valueOf(pPage));
    if (pFrom != null) requestParams.put("from", pFrom);
    if (pTo != null) requestParams.put("to", pTo);

    String sig = generateSignature(requestParams);
    StringBuilder stringBuilder = new StringBuilder(Constants.API_BASE_URL);
        for (Map.Entry<String, String> e : requestParams.entrySet()) {
            stringBuilder.append(e.getKey()).append("=").append(URLEncoder.encode(e.getValue())).append("&");
    }

    stringBuilder.append("api_sig=").append(sig);
    stringBuilder.append("&format=json");

    return new URL(stringBuilder.toString());
}

    private String generateSignature(Map<String, String> params) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> e : params.entrySet()) {
            stringBuilder.append(e.getKey()).append(e.getValue());
        }
        stringBuilder.append(SECRET);
        return new String(Hex.encodeHex(DigestUtils.md5(stringBuilder.toString())));
    }
}
