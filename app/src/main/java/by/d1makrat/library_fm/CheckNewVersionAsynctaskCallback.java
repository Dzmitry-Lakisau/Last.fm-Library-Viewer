package by.d1makrat.library_fm;

public interface CheckNewVersionAsynctaskCallback {
    void onSuccess(Integer latestVersion);
    void onException(Exception exception);
}
