package by.d1makrat.library_fm.broadcast_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import by.d1makrat.library_fm.HttpsClient;
import by.d1makrat.library_fm.R;

public class NetworkStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //TODO {}
        if (!HttpsClient.isNetworkAvailable())
            Toast.makeText(context, context.getResources().getString(R.string.offline_mode_message), Toast.LENGTH_SHORT).show();
    }
}