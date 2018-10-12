package by.d1makrat.library_fm.presenter.fragment.scrobble

import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.asynctask.GetItemsAsyncTask
import by.d1makrat.library_fm.model.Scrobble
import by.d1makrat.library_fm.operation.RecentScrobblesOperation

class RecentScrobblesPresenter(from: Long, to: Long): ScrobblesPresenter(from, to) {

    init {
        mUrlForBrowser = AppContext.getInstance().user.url + "/library"
    }

    override fun performOperation() {
        val recentScrobblesOperation = RecentScrobblesOperation(mPage, from, to)
        val getItemsAsyncTask = GetItemsAsyncTask<Scrobble>(this)
        getItemsAsyncTask.execute(recentScrobblesOperation)
    }
}
