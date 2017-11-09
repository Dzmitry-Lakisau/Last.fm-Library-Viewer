package by.d1makrat.library_fm;

import android.content.Context;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.TreeMap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;

public class NetworkRequester {

    private URL mURL;
    private final String SECRET = "c51dd917013880fd9e6f7c20e98ab3e1";

    public NetworkRequester(TreeMap<String, String> pRequestParams, Context pContext) throws MalformedURLException{
        //URL MUST be encoded. hash is not.

        AppSettings appSettings = new AppSettings(pContext);
        String sessionKey = appSettings.getSessionKey();
        String username = appSettings.getUsername();
        String limit = appSettings.getLimit();

        pRequestParams.put("api_key", BuildConfig.API_KEY);
        pRequestParams.put("sk", sessionKey);
        pRequestParams.put("user", username);
        pRequestParams.put("limit", String.valueOf(limit));

        String sig = generateSignature(pRequestParams);
        String s = "https://ws.audioscrobbler.com/2.0/?";
        for (Map.Entry<String, String> e : pRequestParams.entrySet()) {
            s += e.getKey() + "=" + URLEncoder.encode(e.getValue()) + "&";//TODO
        }
        s = s.substring(0, s.length() - 1);
        s += "&api_sig=" + sig;
        s += "&format=json";
        mURL = new URL(s);
    }

    private String generateSignature(Map<String, String> params) {
        String s = "";
        for (Map.Entry<String, String> e : params.entrySet()) {
            s += e.getKey() + e.getValue();
        }
        s += SECRET;
        return new String(Hex.encodeHex(DigestUtils.md5(s)));
    }

    public String request(String method) throws SocketTimeoutException, UnknownHostException, SSLException, IOException {
        BufferedReader in;
        InputStreamReader isr;
        HttpURLConnection conn = null;
        StringBuilder sb = null;

        boolean https = true;

        URLConnection c = mURL.openConnection();
        if (https) conn = (HttpsURLConnection) c;
            else conn = (HttpURLConnection) c;
        if (method.equals("POST")) {
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
        } else {
            conn.setRequestMethod("GET");
        }
        conn.setDoInput(true);
        conn.setConnectTimeout(2000);
        conn.setReadTimeout(2000);
        conn.connect();

        if (conn.getResponseCode() > 399) {
            isr = new InputStreamReader(conn.getErrorStream());
        } else if (conn.getResponseCode() == 200) {
            isr = new InputStreamReader(conn.getInputStream());
        }
        else return null;

        in = new BufferedReader(isr);
        sb = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            sb.append(line);
        }
        conn.disconnect();
        return sb.toString();
    }
}
