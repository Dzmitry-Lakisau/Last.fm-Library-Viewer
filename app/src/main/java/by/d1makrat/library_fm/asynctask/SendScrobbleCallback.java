package by.d1makrat.library_fm.asynctask;

public interface SendScrobbleCallback {
    void onException(Exception exception);
    void onSendScrobbleResult(String result);
}