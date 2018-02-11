package by.d1makrat.library_fm.ui.fragment.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import by.d1makrat.library_fm.R;

public class UpdateDialogFragment extends DialogFragment {

    private static final String GOOGLE_PLAY_LINK = "https://play.google.com/store/apps/details?id=";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        @SuppressWarnings("ConstantConditions") AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Activity activity = getActivity();
        if (activity != null) {
            builder = new AlertDialog.Builder(activity, R.style.DialogTheme);

            LayoutInflater inflater = activity.getLayoutInflater();
            @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_update, null);
            builder.setView(view);
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_launcher, null);
//            if (drawable != null) {
//                drawable.setBounds(100, 0, 0, 0);
                ((TextView) view.findViewById(R.id.textView)).setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
//            }

            builder.setPositiveButton("Download now", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_PLAY_LINK + getActivity().getPackageName()));
                    startActivity(intent);
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        }

        return builder.create();
    }
}
