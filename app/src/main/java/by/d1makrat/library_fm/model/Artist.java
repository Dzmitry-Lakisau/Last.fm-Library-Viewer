package by.d1makrat.library_fm.model;

public class Artist extends RankedItem{

    private String name;
    private String listenersCount;
    private String url;
    private String imageUri;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getListenersCount() {
        return listenersCount;
    }

    public void setListenersCount(String listenersCount) {
        this.listenersCount = listenersCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
