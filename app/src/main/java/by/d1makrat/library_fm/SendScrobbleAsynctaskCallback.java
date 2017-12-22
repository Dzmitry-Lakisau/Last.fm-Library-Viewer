package by.d1makrat.library_fm;

public interface SendScrobbleAsynctaskCallback {
    void onException(Exception exception);
    void onSendScrobbleResult(String result);
}
