package by.d1makrat.library_fm.presenter.fragment

import android.net.Uri

interface ItemsView<T> {
    fun clearList()
    fun populateList(items: List<T>?)
    fun removeAllHeadersAndFooters()
    fun showEmptyHeader(message: String)
    fun showHeader()
    fun showFooter()
    fun showError(message: String?)
    fun showAllIsLoaded()
    fun openBrowser(uri: Uri)
}
