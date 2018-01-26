package by.d1makrat.library_fm.model;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class User implements IUser {

    private String username;

    private String playcount;

    private String registered;

    private String url;

    private String avatarUri;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPlaycount() {
        return playcount;
    }

    public void setPlaycount(String playcount) {
        this.playcount = playcount;
    }

    @Override
    public String getRegistered() {
        Calendar mydate = Calendar.getInstance();
        mydate.setTimeInMillis(TimeUnit.SECONDS.toMillis(Long.valueOf(registered)));
        return mydate.get(Calendar.DAY_OF_MONTH) + " " + mydate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH) + " " + mydate.get(Calendar.YEAR);
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAvatarUri() {
        return avatarUri;
    }

    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }
}
