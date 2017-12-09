package by.d1makrat.library_fm;

import java.util.List;

import by.d1makrat.library_fm.model.Scrobble;

public interface GetScrobblesAsynctaskCallback {
    void onLoadingScrobblesSuccessful(List<Scrobble> scrobbles);
    void onException(Exception exception);
}
