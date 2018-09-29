package by.d1makrat.library_fm.view.fragment

interface TopItemsView<T>: ItemsView<T> {
    fun hideListHead()
    fun showListHead(itemCount: Int)
    fun showEmptyHeader()
}
