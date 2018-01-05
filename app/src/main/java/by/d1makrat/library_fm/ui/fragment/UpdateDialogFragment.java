package by.d1makrat.library_fm.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import by.d1makrat.library_fm.R;

public class UpdateDialogFragment extends android.support.v4.app.DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog, null);
        adb.setView(view);

        TextView title =  new TextView(getActivity().getApplicationContext());
        title.setText("Update");
        title.setIncludeFontPadding(true);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(24);
        title.setTextColor(Color.BLACK);
        adb.setCustomTitle(title);
//        adb.setTitle("Show scrobbles");
        //// кнопка положительного ответа
        adb.setPositiveButton("Download now", myClickListener);

        // кнопка отрицательного ответа
        adb.setNegativeButton("Cancel", myClickListener);

        return adb.create();
    }

    private static final String GOOGLE_PLAY_LINK = "https://play.google.com/store/apps/details?id=";

    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                // положительная кнопка
                case DialogInterface.BUTTON_POSITIVE:
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_PLAY_LINK + getActivity().getPackageName()));
                    startActivity(intent);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };
}