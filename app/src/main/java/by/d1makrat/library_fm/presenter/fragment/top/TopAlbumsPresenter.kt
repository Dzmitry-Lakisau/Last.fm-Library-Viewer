package by.d1makrat.library_fm.presenter.fragment.top

import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.asynctask.GetTopItemsAsyncTask
import by.d1makrat.library_fm.model.Album
import by.d1makrat.library_fm.model.Period
import by.d1makrat.library_fm.operation.TopAlbumsOperation

class TopAlbumsPresenter(period: String): TopItemsPresenter<Album>(period) {
    init {
        mUrlForBrowser = AppContext.getInstance().user.url + "/library/albums" + Period().getSuffixForUrl(period)
    }

    override fun performOperation() {
        val topAlbumsOperation = TopAlbumsOperation(period, mPage)
        val getTopAlbumsAsyncTask = GetTopItemsAsyncTask(this)
        getTopAlbumsAsyncTask.execute(topAlbumsOperation)
    }
}
