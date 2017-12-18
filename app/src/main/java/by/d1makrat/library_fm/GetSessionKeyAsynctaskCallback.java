package by.d1makrat.library_fm;

public interface GetSessionKeyAsynctaskCallback {
    void onSessionKeyGranted(String sessionKey);
    void onException(Exception exception);
}