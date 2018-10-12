package by.d1makrat.library_fm.broadcast_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.ui.CenteredToast;
import by.d1makrat.library_fm.utils.ConnectionChecker;

public class NetworkStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!ConnectionChecker.isNetworkAvailable()) {
            CenteredToast.show(context, R.string.offline_mode_message, Toast.LENGTH_SHORT);
        }
    }
}
