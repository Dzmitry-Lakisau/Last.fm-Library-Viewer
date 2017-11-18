package by.d1makrat.library_fm;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;

import java.util.Calendar;
import java.util.TimeZone;

public class AboutDialogFragment extends android.support.v4.app.DialogFragment {

    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_about, null);

        adb.setView(view);

        final TextView textView = (TextView) view.findViewById(R.id.about_text_title);
        textView.setText("About " + getString(R.string.app_name));

        return adb.create();
    }

}