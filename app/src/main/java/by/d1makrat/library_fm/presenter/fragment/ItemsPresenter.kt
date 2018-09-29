package by.d1makrat.library_fm.presenter.fragment

import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.view.fragment.ItemsView


abstract class ItemsPresenter<T, V: ItemsView<T>> {

    protected var view: V? = null

    protected var isLoading = false
    protected var allIsLoaded = false
    protected var mPage = 0//1

    protected var mUrlForBrowser: String? = null

    fun detachView(){
        view = null
    }

    fun attachView(view: V){
        this.view = view
    }

    protected abstract fun performOperation()

    open fun checkIfAllIsLoaded(size: Int) {
        if (size < AppContext.getInstance().limit){
            allIsLoaded = true
            view?.showAllIsLoaded()
        }
    }

    fun onException(exception: Exception) {//TODO ? add footer with retry behavior
        isLoading = false

        mPage--

        view?.showError(exception.message)
    }

    fun loadItems() {
        if (!isLoading && !allIsLoaded) {
            mPage++

            if (mPage == 1) {
                view?.showHeader()
            }
            else {
                view?.showFooter()
            }

            performOperation()
        }
    }
}
