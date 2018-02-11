package by.d1makrat.library_fm.ui.fragment.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import by.d1makrat.library_fm.R;

public class AboutDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        Activity activity = getActivity();
        if (activity != null) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);

            @SuppressLint("InflateParams") View view = activity.getLayoutInflater().inflate(R.layout.dialog_about, null);
            alertDialogBuilder.setView(view);

            final TextView textView = view.findViewById(R.id.about_text_title);
            textView.setText(getString(R.string.about_app, getResources().getString(R.string.app_name)));

            return alertDialogBuilder.create();
        } else return super.onCreateDialog(savedInstance);
    }
}
