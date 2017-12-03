package by.d1makrat.library_fm.model;

public class Scrobble{

    private String mArtist;
    private String mTrack;
    private String mAlbum;
    private String mImageUri;
    private Date mDate;

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

    public void setDate(Date mDate) {
        this.mDate = mDate;
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

//    public Uri getImageUriBySize(ResolutionOfImage pResolutionOfImage){
//        return mImages.get(pResolutionOfImage.ordinal()).getImageURI();
//    }
//
//    public void setImageUriBySize(ResolutionOfImage pResolutionOfImage, String pUri){
//        mImages.get(pResolutionOfImage.ordinal()).setImageURI(pUri);
//    }

    public Date getDate(){
        return mDate;
    }
}