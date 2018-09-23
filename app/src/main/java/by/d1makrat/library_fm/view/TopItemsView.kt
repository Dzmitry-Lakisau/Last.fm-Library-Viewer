package by.d1makrat.library_fm.view

interface TopItemsView<T>: ItemsView<T> {
    fun hideListHead()
    fun showListHead(itemCount: Int)
    fun showEmptyHeader()
}
