package by.d1makrat.library_fm;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;

public class HttpsClient {

    public String request(URL pUrl, String pMethod) throws SocketTimeoutException, UnknownHostException, SSLException, IOException {

        HttpURLConnection httpsURLConnection = (HttpsURLConnection) pUrl.openConnection();

        if (pMethod.equals("POST")) {
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setDoOutput(true);
        } else {
            httpsURLConnection.setRequestMethod("GET");
        }
        httpsURLConnection.setDoInput(true);
        httpsURLConnection.setConnectTimeout(2000);
        httpsURLConnection.setReadTimeout(2000);
        httpsURLConnection.connect();

        InputStreamReader inputStreamReader;
        if (httpsURLConnection.getResponseCode() > 399) {
            inputStreamReader = new InputStreamReader(httpsURLConnection.getErrorStream());
        } else if (httpsURLConnection.getResponseCode() == 200) {
            inputStreamReader = new InputStreamReader(httpsURLConnection.getInputStream());
        }
        else return null;

        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        httpsURLConnection.disconnect();

        return stringBuilder.toString();
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) AppContext.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
