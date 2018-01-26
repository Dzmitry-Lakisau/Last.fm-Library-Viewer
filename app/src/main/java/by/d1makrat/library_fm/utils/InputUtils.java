package by.d1makrat.library_fm.utils;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

public class InputUtils {

    public static void hideKeyboard(final Activity pActivity) {
        if (pActivity != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) pActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (pActivity.getCurrentFocus() != null && inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(pActivity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }
}