package by.d1makrat.library_fm.model;

public class Artist{

    private String mName;
    private String mListenersCount;
    private String mUrl;
    private String mImageUri;

    public String getName() {
        return mName;
    }

    public void setName(String pName) {
        mName = pName;
    }

    public String getListenersCount() {
        return mListenersCount;
    }

    public void setListenersCount(String pListenersCount) {
        mListenersCount = pListenersCount;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String pUrl) {
        mUrl = pUrl;
    }

    public String getImageUri() {
        return mImageUri;
    }

    public void setImageUri(String pImageUri) {
        mImageUri = pImageUri;
    }
}