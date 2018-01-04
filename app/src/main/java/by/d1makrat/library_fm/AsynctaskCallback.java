package by.d1makrat.library_fm;

import java.util.List;

import by.d1makrat.library_fm.model.RankedItem;
import by.d1makrat.library_fm.model.Scrobble;

//TODO refactor to Generic interface like operation
public interface AsynctaskCallback {
    void onException(Exception exception);
    void onLoadingRankedItemsSuccessful(List<RankedItem> result);
    void onLoadingScrobblesSuccessful(List<Scrobble> result);
}