package by.d1makrat.library_fm.presenter.fragment.top

import by.d1makrat.library_fm.asynctask.GetTopItemsCallback
import by.d1makrat.library_fm.model.TopItems
import by.d1makrat.library_fm.presenter.fragment.ItemsPresenter

abstract class TopItemsPresenter<T>(val period: String): ItemsPresenter<T, TopItemsView<T>>(), GetTopItemsCallback<T> {

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
                view?.showListHead(result.totalCount)

                checkIfAllIsLoaded(size)
            }
            view?.getListItemsCount() == 0 -> view?.showEmptyHeader()
            else -> checkIfAllIsLoaded(size)
        }
    }
}
