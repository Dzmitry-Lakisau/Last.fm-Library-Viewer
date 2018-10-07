package by.d1makrat.library_fm.presenter.fragment.top

import android.net.Uri
import by.d1makrat.library_fm.asynctask.GetTopItemsCallback
import by.d1makrat.library_fm.model.Period
import by.d1makrat.library_fm.model.TopItems
import by.d1makrat.library_fm.presenter.fragment.ItemsPresenter
import by.d1makrat.library_fm.view.fragment.TopItemsView

abstract class TopItemsPresenter<T>(val period: String): ItemsPresenter<T, TopItemsView<T>>(), GetTopItemsCallback<T> {

    private var totalCount: Int = 0

    fun onRefresh() {
        if (!isLoading) {
            allIsLoaded = false


            view?.clearList()
            view?.hideListHead()

            mPage = 0
            loadItems()
        }
    }


    override fun onLoadingSuccessful(result: TopItems<T>) {
        isLoading = false

        view?.removeAllHeadersAndFooters()

        val size = result.items.size
        when {
            size > 0 -> {
                view?.populateList(result.items)
                totalCount = result.totalCount
                view?.showListHead(totalCount)

                checkIfAllIsLoaded(size)
            }
            view?.getListItemsCount() == 0 -> view?.showEmptyHeader()
            else -> checkIfAllIsLoaded(size)
        }
    }

    fun onCreatingNewView(){
        view?.hideListHead()
        loadItems()
    }

    fun onShowingFromBackStack(){
        view?.showListHead(totalCount)
    }

    fun onOpenInBrowser() {
        view?.openBrowser(Uri.parse(mUrlForBrowser))
    }
}
