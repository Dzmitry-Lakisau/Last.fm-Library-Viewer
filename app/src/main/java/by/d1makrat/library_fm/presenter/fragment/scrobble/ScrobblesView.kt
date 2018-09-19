package by.d1makrat.library_fm.presenter.fragment.scrobble

import by.d1makrat.library_fm.presenter.fragment.ItemsWithHeadView

interface ScrobblesView<T>: ItemsWithHeadView<T> {
    fun getListItemsCount(): Int
    fun showFilterDialog()
}
