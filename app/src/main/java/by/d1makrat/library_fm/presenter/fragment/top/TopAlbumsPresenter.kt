package by.d1makrat.library_fm.presenter.fragment.top

import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.asynctask.GetTopAlbumsAsyncTask
import by.d1makrat.library_fm.asynctask.GetTopAlbumsCallback
import by.d1makrat.library_fm.model.Album
import by.d1makrat.library_fm.model.Period
import by.d1makrat.library_fm.model.TopAlbums
import by.d1makrat.library_fm.operation.TopAlbumsOperation

class TopAlbumsPresenter(period: String): TopItemsPresenter<Album>(period), GetTopAlbumsCallback {
    init {
        mUrlForBrowser = AppContext.getInstance().user.url + "/library/albums" + Period().getSuffixForUrl(period)
    }

    override fun performOperation() {
        val topAlbumsOperation = TopAlbumsOperation(period, mPage)
        val getTopAlbumsAsyncTask = GetTopAlbumsAsyncTask(this)
        getTopAlbumsAsyncTask.execute(topAlbumsOperation)
    }

    override fun onLoadingSuccessful(result: TopAlbums) {
        isLoading = false

        view?.removeAllHeadersAndFooters()

        val size = result.albums.size
        when {
            size > 0 -> {
                view?.populateList(result.albums)
                view?.showListHead(result.totalCount)

                checkIfAllIsLoaded(size)
            }
            view?.getListItemsCount() == 0 -> view?.showEmptyHeader()
            else -> checkIfAllIsLoaded(size)
        }
    }
}
