package by.d1makrat.library_fm.presenter.fragment

import android.net.Uri
import by.d1makrat.library_fm.asynctask.GetItemsAsyncTask
import by.d1makrat.library_fm.asynctask.GetItemsCallback
import by.d1makrat.library_fm.model.Artist
import by.d1makrat.library_fm.operation.SearchArtistOperation
import by.d1makrat.library_fm.view.fragment.SearchArtistView

class SearchArtistPresenter: ItemsPresenter<Artist, SearchArtistView<Artist>>(), GetItemsCallback<Artist> {

    private var searchQuery: String? = null

    init {
        mUrlForBrowser = "https://www.last.fm/search/artists?q="
    }

    override fun onLoadingSuccessful(result: List<Artist>) {
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
        val searchArtistOperation = SearchArtistOperation(searchQuery, mPage)
        val getItemsAsyncTask = GetItemsAsyncTask(this)
        getItemsAsyncTask.execute(searchArtistOperation)
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
