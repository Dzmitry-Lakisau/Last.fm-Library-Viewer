package by.d1makrat.library_fm.ui.fragment.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.ui.CenteredToast;

import static by.d1makrat.library_fm.Constants.DATE_LONG_DEFAUT_VALUE;
import static by.d1makrat.library_fm.Constants.FILTER_DIALOG_FROM_BUNDLE_KEY;
import static by.d1makrat.library_fm.Constants.FILTER_DIALOG_TO_BUNDLE_KEY;

public class FilterDialogFragment extends DialogFragment {

    public static final Long DATE_LASTFM_LAUNCHED = 1016582400L;

    private Long mFrom, mTo;
    private Calendar mCalendarFrom, mCalendarTo;

    public interface FilterDialogListener {
       void onFinishFilterDialog(Long pFrom, Long pTo);
    }

    FilterDialogListener mFilterDialogListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment fragment = getTargetFragment();
//        try {
            mFilterDialogListener = (FilterDialogListener) fragment;
//        }
//        catch (ClassCastException e) {
//            //FirebaseCrash.report(e);
//            throw new ClassCastException(fragment.toString() + " must implement FilterDialogListener");
//        }
        mFrom = getArguments().getLong(FILTER_DIALOG_FROM_BUNDLE_KEY);
        if (mFrom > DATE_LONG_DEFAUT_VALUE){
//            mFrom = Long.valueOf(temp);
            mCalendarFrom = Calendar.getInstance(TimeZone.getDefault());
            mCalendarFrom.setTimeInMillis(TimeUnit.SECONDS.toMillis(mFrom));
        }
        mTo = getArguments().getLong(FILTER_DIALOG_TO_BUNDLE_KEY);
        if (mTo > DATE_LONG_DEFAUT_VALUE){
//            mTo = Long.valueOf(temp);
            mCalendarTo = Calendar.getInstance(TimeZone.getDefault());
            mCalendarTo.setTimeInMillis(TimeUnit.SECONDS.toMillis(mTo));
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_filter, null);
        final DatePicker datePicker = view.findViewById(R.id.datePicker_from);
        final DatePicker datePicker2 = view.findViewById(R.id.datePicker_to);

        if (mFrom > DATE_LONG_DEFAUT_VALUE) datePicker.init(mCalendarFrom.get(Calendar.YEAR), mCalendarFrom.get(Calendar.MONTH), mCalendarFrom.get(Calendar.DAY_OF_MONTH), null);
        if (mTo > DATE_LONG_DEFAUT_VALUE) datePicker2.init(mCalendarTo.get(Calendar.YEAR), mCalendarTo.get(Calendar.MONTH), mCalendarTo.get(Calendar.DAY_OF_MONTH), null);
        adb.setView(view);

        TextView title =  new TextView(getActivity().getApplicationContext());
        title.setText(R.string.filter_dialog_title);
        title.setIncludeFontPadding(true);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(getResources().getDimension(R.dimen.filter_dialog_title_text_size));
        title.setTextColor(Color.BLACK);
        adb.setCustomTitle(title);

        adb.setPositiveButton(R.string.filter_dialog_accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatePicker datePicker = view.findViewById(R.id.datePicker_from);
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();
                Calendar calendar_from = Calendar.getInstance();
                calendar_from.set(year, month, day, 0, 0, 0);
                mFrom = TimeUnit.MILLISECONDS.toSeconds(calendar_from.getTimeInMillis());

                datePicker = view.findViewById(R.id.datePicker_to);
                year = datePicker.getYear();
                month = datePicker.getMonth();
                day = datePicker.getDayOfMonth();
                Calendar calendar_to = Calendar.getInstance();
                calendar_to.set(year, month, day, 23, 59, 59);
                mTo = TimeUnit.MILLISECONDS.toSeconds(calendar_to.getTimeInMillis());
                if (mFrom > mTo || mTo < DATE_LASTFM_LAUNCHED ){
                    CenteredToast.show(getContext(), R.string.filter_dialog_wrong_input, Toast.LENGTH_SHORT);
                }
                else mFilterDialogListener.onFinishFilterDialog(mFrom, mTo);
            }
        });

        adb.setNeutralButton(R.string.filter_dialog_all, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mFilterDialogListener.onFinishFilterDialog(DATE_LONG_DEFAUT_VALUE, DATE_LONG_DEFAUT_VALUE);
            }
        });

        adb.setNegativeButton(R.string.filter_dialog_cancel, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
        });

        view.findViewById(R.id.equate_button);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker2.updateDate(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
            }
        });

        return adb.create();
    }
}