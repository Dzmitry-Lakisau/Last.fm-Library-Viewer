package by.d1makrat.library_fm.model;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class User {

    private String username;

    private String playcount;

    private String registered;

    private String url;

    private String avatarUri;

    public User(String pUsername, String pPlaycount, String pRegistered, String pUrl, String pAvatarUrl) {
        username = pUsername;
        playcount = pPlaycount;
        registered = pRegistered;
        url = pUrl;
        avatarUri = pAvatarUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPlaycount() {
        return playcount;
    }

    public String getRegistered() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(TimeUnit.SECONDS.toMillis(Long.valueOf(registered)));
        return calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH) + " " + calendar.get(Calendar.YEAR);
    }

    public String getUrl() {
        return url;
    }

    public String getAvatarUrl() {
        return avatarUri;
    }
}
