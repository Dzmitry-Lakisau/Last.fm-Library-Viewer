package by.d1makrat.library_fm.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;

import java.util.Calendar;
import java.util.TimeZone;

import by.d1makrat.library_fm.R;

public class FilterDialogFragment extends android.support.v4.app.DialogFragment {

    private View view;
    private Long from, to;
    private Calendar calendar_from, calendar_to;

    public interface DialogListener {
       public void onFinishEditDialog(String from, String to);
    }

    DialogListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v4.app.Fragment fragment = getTargetFragment();
        try {
            mListener = (DialogListener) fragment;
        }
        catch (ClassCastException e) {
            //FirebaseCrash.report(e);
            throw new ClassCastException(fragment.toString() + " must implement DialogListener");
        }
        String temp = getArguments().getString("from");
        if (temp!=null){
            from = Long.valueOf(temp);
            calendar_from = Calendar.getInstance(TimeZone.getDefault());
            calendar_from.setTimeInMillis(from*1000);
        }
        temp = getArguments().getString("to");
        if (temp!=null){
            to = Long.valueOf(temp);
            calendar_to = Calendar.getInstance(TimeZone.getDefault());
            calendar_to.setTimeInMillis(to*1000);
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog, null);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker_from);
        final DatePicker datePicker2 = (DatePicker) view.findViewById(R.id.datePicker_to);

        if (from!=null) datePicker.init(calendar_from.get(calendar_from.YEAR), calendar_from.get(calendar_from.MONTH), calendar_from.get(calendar_from.DAY_OF_MONTH), null);
        if (to!=null) datePicker2.init(calendar_to.get(Calendar.YEAR), calendar_to.get(Calendar.MONTH), calendar_to.get(Calendar.DAY_OF_MONTH), null);
        adb.setView(view);

        TextView title =  new TextView(getActivity().getApplicationContext());
        title.setText("Show scrobbles");
        title.setIncludeFontPadding(true);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(24);
        title.setTextColor(Color.BLACK);
        adb.setCustomTitle(title);
//        adb.setTitle("Show scrobbles");
        //// кнопка положительного ответа
        adb.setPositiveButton("Accept", myClickListener);// new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                mListener.onDialogPositiveClick(FilterDialogFragment.this);
//            }
//        });
        adb.setNeutralButton("All", myClickListener);// new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                // Send the negative button event back to the host activity
//                mListener.onDialogNeutralClick(FilterDialogFragment.this);
//            }
//        });

        // кнопка отрицательного ответа
        adb.setNegativeButton("Cancel", myClickListener);// new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                // Send the negative button event back to the host activity
//                mListener.onDialogNegativeClick(FilterDialogFragment.this);
//            }
//        });

        view.findViewById(R.id.equate_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker2.updateDate(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
            }
        });

        return adb.create();
    }

        DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
//                GetScrobblesListTask task = new GetScrobblesListTask();
                switch (which) {
                    // положительная кнопка
                    case DialogInterface.BUTTON_POSITIVE:
                        DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker_from);
                        int year = datePicker.getYear();
                        int month = datePicker.getMonth();
                        int day = datePicker.getDayOfMonth();
                        Calendar calendar_from = Calendar.getInstance();
                        calendar_from.set(year, month, day, 0, 0, 0);
//                        long temp = new Date().getTimezoneOffset()*60;
                        from = calendar_from.getTimeInMillis() / 1000;// + temp;

                        datePicker = (DatePicker) view.findViewById(R.id.datePicker_to);
                        year = datePicker.getYear();
                        month = datePicker.getMonth();
                        day = datePicker.getDayOfMonth();
                        Calendar calendar_to = Calendar.getInstance();
                        calendar_to.set(year, month, day, 23, 59, 59);
                        to = calendar_to.getTimeInMillis() / 1000;// + new Date().getTimezoneOffset()*60;
                        if (from>to){
                            Toast toast = Toast.makeText(view.getContext(), "Wrong dates", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else mListener.onFinishEditDialog(String.valueOf(from), String.valueOf(to));

                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                    case DialogInterface.BUTTON_NEUTRAL:
                        mListener.onFinishEditDialog(null, null);
                        break;
                }
            }
        };
}