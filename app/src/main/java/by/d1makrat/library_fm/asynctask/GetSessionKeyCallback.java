package by.d1makrat.library_fm.asynctask;

public interface GetSessionKeyCallback {
    void onSessionKeyGranted(String sessionKey);
    void onException(Exception exception);
}