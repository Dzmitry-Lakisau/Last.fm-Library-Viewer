package by.d1makrat.library_fm;

//TODO refactor all callbacks to generic callback for Model and Exception
public interface SendScrobbleAsynctaskCallback {
    void onException(Exception exception);
    void onSendScrobbleResult(String result);
}
