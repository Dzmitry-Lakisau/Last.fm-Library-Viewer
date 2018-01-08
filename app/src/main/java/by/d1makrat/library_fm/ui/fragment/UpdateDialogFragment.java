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

    private static final String GOOGLE_PLAY_LINK = "https://play.google.com/store/apps/details?id=";

    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_update, null);
        adb.setView(view);

        adb.setPositiveButton("Download now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_PLAY_LINK + getActivity().getPackageName()));
                startActivity(intent);
            }
        });


        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        return adb.create();
    }
}