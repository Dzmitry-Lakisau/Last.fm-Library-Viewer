package by.d1makrat.library_fm.view.fragment

interface SearchArtistView<T>: ItemsView<T> {
    fun hideKeyboard()
    fun showEmptyHeader()
}
