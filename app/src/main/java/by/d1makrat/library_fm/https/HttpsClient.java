package by.d1makrat.library_fm.https;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import by.d1makrat.library_fm.AppContext;

public class HttpsClient {

    private static final int TIMEOUT = 1000;

    public String request(URL pUrl, RequestMethod pRequestMethod) throws IOException{

        HttpURLConnection httpsURLConnection = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder;

        try {
            httpsURLConnection = (HttpsURLConnection) pUrl.openConnection();

            if (pRequestMethod.equals(RequestMethod.POST)) {
                httpsURLConnection.setDoOutput(true);
            }

            httpsURLConnection.setRequestMethod(pRequestMethod.name());
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setConnectTimeout(TIMEOUT);
            httpsURLConnection.setReadTimeout(TIMEOUT);
            httpsURLConnection.connect();

            if (httpsURLConnection.getResponseCode() >= HttpURLConnection.HTTP_BAD_REQUEST) {
                inputStreamReader = new InputStreamReader(httpsURLConnection.getErrorStream());
            } else{
                inputStreamReader = new InputStreamReader(httpsURLConnection.getInputStream());
            }

            bufferedReader = new BufferedReader(inputStreamReader);
            stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        finally {
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (httpsURLConnection != null) {
                httpsURLConnection.disconnect();
            }
        }

        return stringBuilder.toString();
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) AppContext.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnected();
    }
}
