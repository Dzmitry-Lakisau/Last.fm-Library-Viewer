package by.d1makrat.library_fm.model;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Scrobble{

    private String mArtist;
    private String mTrack;
    private String mAlbum;
    private String mImageUri;
    private long mUnixDate;

    public void setArtist(String pArtist){
        mArtist = pArtist;
    }

    public void setTrackTitle(String mTrack) {
        this.mTrack = mTrack;
    }

    public void setAlbum(String pAlbum) {
        mAlbum = pAlbum;
    }

    public void setImageUri(String pImageUri) {
        mImageUri = pImageUri;
    }

    public void setDate(long pUnixDate) {
        mUnixDate = pUnixDate;
    }

    public String getArtist(){
        return mArtist;
    }

    public String getTrackTitle() {
        return mTrack;
    }

    public String getAlbum()  {
        return mAlbum;
    }

    public String getImageUri() {
        return mImageUri;
    }

    public String getFormattedDate(){
        java.util.Date date = new java.util.Date (TimeUnit.SECONDS.toMillis(mUnixDate));
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy, HH:mm:ss", Locale.ENGLISH);
        return sdf.format(date);
    }

    public long getDate(){
        return mUnixDate;
    }
}