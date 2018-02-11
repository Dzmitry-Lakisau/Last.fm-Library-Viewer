package by.d1makrat.library_fm.utils;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.R;

import static by.d1makrat.library_fm.Constants.DATE_LONG_DEFAUT_VALUE;

public class DateUtils {

    private static final String FORMATTING_DATE_PATTERN_FOR_URL = "yyyy-MM-dd";
    private static final String FORMATTING_DATE_PATTERN_FOR_MESSAGE = "d MMM yyyy";

    public static String getMessageFromTimestamps(int pScrobbleCount, Long pFrom, Long pTo) {

        if (pFrom.equals(DATE_LONG_DEFAUT_VALUE) && pTo.equals(DATE_LONG_DEFAUT_VALUE)) {
            if (pScrobbleCount > 0) {
                return AppContext.getInstance().getResources().getQuantityString(R.plurals.scrobbles_count, pScrobbleCount, pScrobbleCount);
            }
            else {
                return AppContext.getInstance().getString(R.string.no_scrobbles);
            }
        }
        else {
            Date date_from = new Date(TimeUnit.SECONDS.toMillis(pFrom));
            Date date_to = new Date(TimeUnit.SECONDS.toMillis(pTo));

            String string_from = new SimpleDateFormat(FORMATTING_DATE_PATTERN_FOR_MESSAGE, Locale.ENGLISH).format(date_from);
            String string_to = new SimpleDateFormat(FORMATTING_DATE_PATTERN_FOR_MESSAGE, Locale.ENGLISH).format(date_to);

            if (pScrobbleCount > 0) {
                return AppContext.getInstance().getResources().getQuantityString(R.plurals.scrobbles_count_within_period, pScrobbleCount, pScrobbleCount, string_from, string_to);
            }
            else {
                return AppContext.getInstance().getString(R.string.no_scrobbles_within_period, string_from, string_to);
            }
        }
    }

    @NonNull
    public static String getUrlFromTimestamps(String pUrlForBrowser, Long pFrom, Long pTo) {

        return pUrlForBrowser.concat("?from=" +
                new SimpleDateFormat(FORMATTING_DATE_PATTERN_FOR_URL, Locale.ENGLISH).format(new Date(TimeUnit.SECONDS.toMillis(pFrom))
                + "&to=" + new SimpleDateFormat(FORMATTING_DATE_PATTERN_FOR_URL, Locale.ENGLISH).format(new Date(TimeUnit.SECONDS.toMillis(pTo)))));
    }
}
