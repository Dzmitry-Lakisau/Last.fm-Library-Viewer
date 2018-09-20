package by.d1makrat.library_fm.asynctask;

import by.d1makrat.library_fm.model.TopArtists;

public interface GetTopArtistsCallback {
    void onException(Exception exception);
    void onLoadingSuccessful(TopArtists result);
}
