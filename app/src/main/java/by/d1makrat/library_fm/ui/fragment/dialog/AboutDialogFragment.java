package by.d1makrat.library_fm.ui.fragment.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import by.d1makrat.library_fm.R;

public class AboutDialogFragment extends android.support.v4.app.DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_about, null);

        adb.setView(view);

        final TextView textView = view.findViewById(R.id.about_text_title);
        textView.setText(String.format(getString(R.string.about_app), getResources().getString(R.string.app_name)));

        return adb.create();
    }

}