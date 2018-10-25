package by.d1makrat.library_fm.utils;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import by.d1makrat.library_fm.AppContext;
import by.d1makrat.library_fm.R;
import by.d1makrat.library_fm.model.FilterRange;

public class DateUtils {

    private static final String FORMATTING_DATE_PATTERN_FOR_URL = "yyyy-MM-dd";
    private static final String FORMATTING_DATE_PATTERN_FOR_MESSAGE = "d MMM yyyy";

    public static String getMessageFromTimestamps(int pScrobbleCount, FilterRange filterRange) {

        if (filterRange.getStartOfPeriod() == null && filterRange.getEndOfPeriod() == null) {
            if (pScrobbleCount > 0) {
                return AppContext.getInstance().getResources().getQuantityString(R.plurals.scrobbles_count, pScrobbleCount, pScrobbleCount);
            }
            else {
                return AppContext.getInstance().getString(R.string.no_scrobbles);
            }
        }
        else {
            Date dateFrom = new Date(TimeUnit.SECONDS.toMillis(filterRange.getStartOfPeriod()));
            Date dateTo = new Date(TimeUnit.SECONDS.toMillis(filterRange.getEndOfPeriod()));

            String stringFrom = new SimpleDateFormat(FORMATTING_DATE_PATTERN_FOR_MESSAGE, Locale.ENGLISH).format(dateFrom);
            String stringTo = new SimpleDateFormat(FORMATTING_DATE_PATTERN_FOR_MESSAGE, Locale.ENGLISH).format(dateTo);

            if (pScrobbleCount > 0) {
                return AppContext.getInstance().getResources().getQuantityString(R.plurals.scrobbles_count_within_period, pScrobbleCount, pScrobbleCount, stringFrom, stringTo);
            }
            else {
                return AppContext.getInstance().getString(R.string.no_scrobbles_within_period, stringFrom, stringTo);
            }
        }
    }

    @NonNull
    public static String getUrlFromTimestamps(String pUrlForBrowser, FilterRange filterRange) {

        if (filterRange.getStartOfPeriod() == null && filterRange.getEndOfPeriod() == null) {
            return pUrlForBrowser;
        } else {
            Date dateFrom = new Date(TimeUnit.SECONDS.toMillis(filterRange.getStartOfPeriod()));
            Date dateTo = new Date(TimeUnit.SECONDS.toMillis(filterRange.getEndOfPeriod()));

            String stringFrom = new SimpleDateFormat(FORMATTING_DATE_PATTERN_FOR_URL, Locale.ENGLISH).format(dateFrom);
            String stringTo = new SimpleDateFormat(FORMATTING_DATE_PATTERN_FOR_URL, Locale.ENGLISH).format(dateTo);

            return pUrlForBrowser.concat("?from=" + stringFrom + "&to=" + stringTo);
        }
    }

    public static FilterRange getTimeRangesOfDay(long unixDateOfScrobble){
//        timestamp += TimeUnit.MILLISECONDS.toSeconds(Calendar.getInstance().getTimeZone().getOffset(timestamp));
//        long rawDate = timestamp - timestamp % TimeUnit.DAYS.toSeconds(1);
//        long start = rawDate - TimeUnit.MILLISECONDS.toSeconds(Calendar.getInstance().getTimeZone().getOffset(rawDate));
//        long end = start + TimeUnit.DAYS.toSeconds(1) - 1;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(TimeUnit.SECONDS.toMillis(unixDateOfScrobble));

        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long start = TimeUnit.MILLISECONDS.toSeconds(calendar.getTimeInMillis());

        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        long end = TimeUnit.MILLISECONDS.toSeconds(calendar.getTimeInMillis());

        return new FilterRange(start, end);
    }
}
