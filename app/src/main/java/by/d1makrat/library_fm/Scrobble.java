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

    public String getImageUriBySize(String pSize){

        String result = null;

        switch(pSize){
            case "small": result = mImages.get(0).getImageURI(); break;
            case "medium": result = mImages.get(1).getImageURI(); break;
            case "large": result = mImages.get(2).getImageURI(); break;
            case "extralarge": result = mImages.get(3).getImageURI(); break;
        }

        return result;
    }

    public void setImageUriBySize(String pSize, String pUri){

        switch(pSize){
            case "small": mImages.get(0).setImageURI(pUri); break;
            case "medium": mImages.get(1).setImageURI(pUri); break;
            case "large": mImages.get(2).setImageURI(pUri); break;
            case "extralarge": mImages.get(3).setImageURI(pUri); break;
        }
    }

    public Date getDate(){
        return mDate;
    }
}