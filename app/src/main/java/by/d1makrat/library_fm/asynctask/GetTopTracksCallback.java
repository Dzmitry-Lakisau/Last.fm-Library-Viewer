package by.d1makrat.library_fm.asynctask;

import by.d1makrat.library_fm.model.TopTracks;

public interface GetTopTracksCallback {
    void onException(Exception exception);
    void onLoadingSuccessful(TopTracks result);
}
