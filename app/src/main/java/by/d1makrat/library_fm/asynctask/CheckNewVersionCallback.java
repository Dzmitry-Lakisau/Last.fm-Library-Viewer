package by.d1makrat.library_fm.asynctask;

public interface CheckNewVersionCallback {
    void onSuccess(Integer latestVersion);
    void onException(Exception exception);
}