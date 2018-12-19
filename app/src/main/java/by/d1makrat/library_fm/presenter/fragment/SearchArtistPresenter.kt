package by.d1makrat.library_fm.presenter.fragment

import android.net.Uri
import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.model.Artist
import by.d1makrat.library_fm.view.fragment.SearchArtistView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchArtistPresenter: ItemsPresenter<Artist, SearchArtistView<Artist>>() {

    var searchQuery: String? = null
        private set

    init {
        mUrlForBrowser = "https://www.last.fm/search/artists?q="
    }

    fun onLoadingSuccessful(result: List<Artist>) {
        isLoading = false

        view?.removeAllHeadersAndFooters()

        val size = result.size
        when {
            size > 0 -> {
                view?.populateList(result)

                checkIfAllIsLoaded(size)
            }
            view?.getListItemsCount() == 0 -> view?.showEmptyHeader()
            else -> checkIfAllIsLoaded(size)
        }
    }

    override fun performOperation() {
        compositeDisposable.add(
                AppContext.getInstance().retrofitWebService.searchArtist(searchQuery!!, mPage, AppContext.getInstance().limit)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {
                                    onLoadingSuccessful(it.getAll().sortedByDescending { artist -> artist.listenersCount })
                                },
                                {
                                    onException(it)
                                }
                        )
        )
    }

    fun onOpenInBrowser() {
        view?.openBrowser(Uri.parse(mUrlForBrowser + searchQuery))
    }

    fun onSearchButtonPressed(searchQuery: String) {
        this.searchQuery = searchQuery

        if (!isLoading) {
            view?.hideKeyboard()
            view?.clearList()

            allIsLoaded = false

            mPage = 0
            loadItems()
        }
    }
}
