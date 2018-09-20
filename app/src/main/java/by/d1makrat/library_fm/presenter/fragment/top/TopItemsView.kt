package by.d1makrat.library_fm.presenter.fragment.top

import by.d1makrat.library_fm.presenter.fragment.ItemsView

interface TopItemsView<T>: ItemsView<T> {
    fun showListHead(itemCount: String)
    fun showEmptyHeader()
}
