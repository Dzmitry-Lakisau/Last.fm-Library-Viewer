package by.d1makrat.library_fm.presenter.fragment

import android.net.Uri

interface ItemsView<T> {
    fun clearList()
    fun populateList(items: List<T>?)
    fun removeAllHeadersAndFooters()
    fun showHeader()
    fun showFooter()
    fun showError(message: String?)
    fun showAllIsLoaded()
    fun openBrowser(uri: Uri)
    fun getListItemsCount(): Int
    fun hideListHead()
}
