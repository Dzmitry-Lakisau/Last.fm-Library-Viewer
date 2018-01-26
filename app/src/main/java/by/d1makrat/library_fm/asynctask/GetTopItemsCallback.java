package by.d1makrat.library_fm.asynctask;

import by.d1makrat.library_fm.operation.model.TopOperationResult;

public interface GetTopItemsCallback<T> {
    void onException(Exception exception);
    void onLoadingSuccessful(TopOperationResult<T> result);
}