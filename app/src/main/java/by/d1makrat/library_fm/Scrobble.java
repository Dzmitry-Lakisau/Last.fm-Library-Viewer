package by.d1makrat.library_fm;

import java.util.List;

public class Scrobble{

    private Artist mArtist;
    private String mTrack;
    private Album mAlbum;
    private List<Image> mImages;
    private Date mDate;

    public void setArtist(Artist mArtist) {
        this.mArtist = mArtist;
    }

    public void setTrackTitle(String mTrack) {
        this.mTrack = mTrack;
    }

    public void setAlbum(Album mAlbum) {
        this.mAlbum = mAlbum;
    }

    public void setImage(List<Image> mImages) {
        this.mImages = mImages;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }


    public Artist getArtist(){
        return mArtist;
    }


    public String getTrackTitle() {
        return mTrack;
    }


    public Album getAlbum()  {
        return mAlbum;
    }


//    public List<Image> getImages() {
//        return mImages;
//    }

    public String getImageUriBySize(ResolutionOfImage pResolutionOfImage){
        return mImages.get(pResolutionOfImage.ordinal()).getImageURI();
    }

    public void setImageUriBySize(ResolutionOfImage pResolutionOfImage, String pUri){
        mImages.get(pResolutionOfImage.ordinal()).setImageURI(pUri);
    }

    public Date getDate(){
        return mDate;
    }
}