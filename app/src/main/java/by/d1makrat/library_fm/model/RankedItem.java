package by.d1makrat.library_fm.model;

public class RankedItem {

    private String primaryField;
    private String secondaryField;
    private String rank;
    private String playcount;
    private String imageUri;

    public String getPrimaryField() {
        return primaryField;
    }

    public void setPrimaryField(String primaryField) {
        this.primaryField = primaryField;
    }

    public String getSecondaryField() {
        return secondaryField;
    }

    public void setSecondaryField(String secondaryField) {
        this.secondaryField = secondaryField;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getPlaycount() {
        return playcount;
    }

    public void setPlaycount(String playcount) {
        this.playcount = playcount;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
