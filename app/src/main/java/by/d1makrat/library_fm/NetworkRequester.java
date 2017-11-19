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

//    public NetworkRequester(TreeMap<String, String> pRequestParams, Context pContext) throws MalformedURLException {
//    }

    public String request(URL pUrl, String pMethod) throws SocketTimeoutException, UnknownHostException, SSLException, IOException {
        BufferedReader in;
        InputStreamReader isr;
        HttpURLConnection conn = null;
        StringBuilder sb = null;

        boolean https = true;

        URLConnection c = pUrl.openConnection();
        if (https) conn = (HttpsURLConnection) c;
            else conn = (HttpURLConnection) c;
        if (pMethod.equals("POST")) {
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
