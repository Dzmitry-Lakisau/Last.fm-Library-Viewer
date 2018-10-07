package by.d1makrat.library_fm.presenter.fragment.top

import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.asynctask.GetTopItemsAsyncTask
import by.d1makrat.library_fm.model.Period
import by.d1makrat.library_fm.model.Track
import by.d1makrat.library_fm.operation.TopTracksOperation

class TopTracksPresenter(period: String): TopItemsPresenter<Track>(period) {
    init {
        mUrlForBrowser = AppContext.getInstance().user.url + "/library/tracks" + Period().getSuffixForUrl(period)
    }

    override fun performOperation() {
        val topTracksOperation = TopTracksOperation(period, mPage)
        val getTopTracksAsyncTask = GetTopItemsAsyncTask(this)
        getTopTracksAsyncTask.execute(topTracksOperation)
    }
}
