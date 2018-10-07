package by.d1makrat.library_fm.presenter.fragment.top

import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.asynctask.GetTopItemsAsyncTask
import by.d1makrat.library_fm.model.Artist
import by.d1makrat.library_fm.model.Period
import by.d1makrat.library_fm.operation.TopArtistsOperation

class TopArtistsPresenter(period: String): TopItemsPresenter<Artist>(period) {
    init {
        mUrlForBrowser = AppContext.getInstance().user.url + "/library/artists" + Period().getSuffixForUrl(period)
    }

    override fun performOperation() {
        val topArtistsOperation = TopArtistsOperation(period, mPage)
        val getTopArtistsAsyncTask = GetTopItemsAsyncTask(this)
        getTopArtistsAsyncTask.execute(topArtistsOperation)
    }
}
