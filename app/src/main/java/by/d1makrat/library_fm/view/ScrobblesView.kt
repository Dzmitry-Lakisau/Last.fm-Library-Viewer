package by.d1makrat.library_fm.view

interface ScrobblesView<T>: ItemsView<T> {
    fun hideListHead()
    fun showFilterDialog()
    fun showEmptyHeader(message: String)
    fun showListHead(message: String)
}
