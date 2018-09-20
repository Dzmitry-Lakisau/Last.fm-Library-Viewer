package by.d1makrat.library_fm.presenter.fragment.top

import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.asynctask.GetTopTracksAsyncTask
import by.d1makrat.library_fm.asynctask.GetTopTracksCallback
import by.d1makrat.library_fm.model.Period
import by.d1makrat.library_fm.model.TopTracks
import by.d1makrat.library_fm.model.Track
import by.d1makrat.library_fm.operation.TopTracksOperation

class TopTracksPresenter(period: String): TopItemsPresenter<Track>(period), GetTopTracksCallback {
    init {
        mUrlForBrowser = AppContext.getInstance().user.url + "/library/tracks" + Period().getSuffixForUrl(period)
    }

    override fun performOperation() {
        val topTracksOperation = TopTracksOperation(period, mPage)
        val getTopTracksAsyncTask = GetTopTracksAsyncTask(this)
        getTopTracksAsyncTask.execute(topTracksOperation)
    }

    override fun onLoadingSuccessful(result: TopTracks) {
        isLoading = false

        view?.removeAllHeadersAndFooters()

        val size = result.tracks.size
        when {
            size > 0 -> {
                view?.populateList(result.tracks)
                view?.showListHead(result.totalCount)

                checkIfAllIsLoaded(size)
            }
            view?.getListItemsCount() == 0 -> view?.showEmptyHeader()
            else -> checkIfAllIsLoaded(size)
        }
    }
}
