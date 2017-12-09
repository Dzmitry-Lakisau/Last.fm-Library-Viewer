package by.d1makrat.library_fm;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkStatusChecker{

    public static final boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) AppContext.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}