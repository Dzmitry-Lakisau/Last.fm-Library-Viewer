package by.d1makrat.library_fm.asynctask;

import by.d1makrat.library_fm.model.TopAlbums;

public interface GetTopAlbumsCallback {
    void onException(Exception exception);
    void onLoadingSuccessful(TopAlbums result);
}
