package by.d1makrat.library_fm.asynctask

import by.d1makrat.library_fm.model.TopItems

interface GetTopItemsCallback<T> {
    fun onException(exception: Exception)
    fun onLoadingSuccessful(result: TopItems<T>)
}
