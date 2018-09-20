package by.d1makrat.library_fm.presenter.fragment.top

import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.asynctask.GetTopArtistsAsyncTask
import by.d1makrat.library_fm.asynctask.GetTopArtistsCallback
import by.d1makrat.library_fm.model.Artist
import by.d1makrat.library_fm.model.Period
import by.d1makrat.library_fm.model.TopArtists
import by.d1makrat.library_fm.operation.TopArtistsOperation

class TopArtistsPresenter(period: String): TopItemsPresenter<Artist>(period), GetTopArtistsCallback {
    init {
        mUrlForBrowser = AppContext.getInstance().user.url + "/library/artists" + Period().getSuffixForUrl(period)
    }

    override fun performOperation() {
        val topArtistsOperation = TopArtistsOperation(period, mPage)
        val getTopArtistsAsyncTask = GetTopArtistsAsyncTask(this)
        getTopArtistsAsyncTask.execute(topArtistsOperation)
    }

    override fun onLoadingSuccessful(result: TopArtists) {
        isLoading = false

        view?.removeAllHeadersAndFooters()

        val size = result.artists.size
        when {
            size > 0 -> {
                view?.populateList(result.artists)
                view?.showListHead(result.totalCount)

                checkIfAllIsLoaded(size)
            }
            view?.getListItemsCount() == 0 -> view?.showEmptyHeader()
            else -> checkIfAllIsLoaded(size)
        }
    }
}
