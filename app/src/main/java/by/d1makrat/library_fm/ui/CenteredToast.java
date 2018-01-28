package by.d1makrat.library_fm.ui;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

public class CenteredToast {

    public static void show(Context context, int stringResource, int duration){
        Toast toast = Toast.makeText(context, context.getString(stringResource), duration);
        TextView textView = toast.getView().findViewById(android.R.id.message);
        if (textView != null) textView.setGravity(Gravity.CENTER);
        toast.show();
    }

    public static void show(Context context, CharSequence text, int duration){
        Toast toast = Toast.makeText(context, text, duration);
        TextView textView = toast.getView().findViewById(android.R.id.message);
        if (textView != null) textView.setGravity(Gravity.CENTER);
        toast.show();
    }
}