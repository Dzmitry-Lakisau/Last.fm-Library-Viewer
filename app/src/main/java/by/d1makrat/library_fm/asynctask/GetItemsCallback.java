package by.d1makrat.library_fm.asynctask;

import java.util.List;

public interface GetItemsCallback<T> {
    void onException(Exception exception);
    void onLoadingSuccessful(List<T> result);
}