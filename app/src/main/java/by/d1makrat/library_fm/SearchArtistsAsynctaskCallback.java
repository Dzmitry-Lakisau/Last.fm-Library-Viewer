package by.d1makrat.library_fm;

import java.util.List;

import by.d1makrat.library_fm.model.Artist;

public interface SearchArtistsAsynctaskCallback {
    void onException(Exception exception);
    void onLoadingSearchArtistsResultsSuccessful(List<Artist> result);
}
