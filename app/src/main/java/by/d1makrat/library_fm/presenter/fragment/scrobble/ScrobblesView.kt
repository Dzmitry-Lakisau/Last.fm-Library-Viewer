package by.d1makrat.library_fm.presenter.fragment.scrobble

import by.d1makrat.library_fm.presenter.fragment.ItemsView

interface ScrobblesView<T>: ItemsView<T> {
    fun showFilterDialog()
    fun showEmptyHeader(message: String)
    fun showListHead(message: String)
}
