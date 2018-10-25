package by.d1makrat.library_fm.view.fragment

import by.d1makrat.library_fm.model.FilterRange

interface ScrobblesView<T>: ItemsView<T> {
    fun hideListHead()
    fun showFilterDialog()
    fun showEmptyHeader(message: String)
    fun showListHead(message: String)
    fun openScrobblesFragment(filterRange: FilterRange)
}
