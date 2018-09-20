package by.d1makrat.library_fm.presenter.fragment.top

import by.d1makrat.library_fm.presenter.fragment.ItemsPresenter

abstract class TopItemsPresenter<T>(val period: String): ItemsPresenter<T, TopItemsView<T>>() {

    fun onRefresh() {
        if (!isLoading) {
            allIsLoaded = false


            view?.clearList()
            view?.hideListHead()

            mPage = 0
            loadItems()
        }
    }
}
