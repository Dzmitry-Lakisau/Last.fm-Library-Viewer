package by.d1makrat.library_fm.presenter.fragment

interface ItemsWithHeadView<T>: ItemsView<T> {
    fun hideListHead()
    fun showListHead(message: String)
}
