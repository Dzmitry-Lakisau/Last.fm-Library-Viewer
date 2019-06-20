package by.d1makrat.library_fm.presenter.fragment

import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.repository.Repository
import by.d1makrat.library_fm.utils.ExceptionHandler
import by.d1makrat.library_fm.view.fragment.ItemsView
import io.reactivex.disposables.CompositeDisposable

abstract class ItemsPresenter<T, V: ItemsView<T>> {

    protected var view: V? = null
    protected val compositeDisposable = CompositeDisposable()
    protected val repository: Repository = AppContext.getInstance().repository

    protected var isLoading = false
    protected var allIsLoaded = false
    protected var mPage = 1

    protected var mUrlForBrowser: String? = null

    fun detachView(){
        view = null
        compositeDisposable.clear()
    }

    fun attachView(view: V){
        this.view = view
    }

    protected abstract fun startLoading()

    open fun checkIfAllIsLoaded(size: Int) {
        if (size < AppContext.getInstance().limit){
            allIsLoaded = true
            view?.showAllIsLoaded()
        }
    }

    fun onException(exception: Throwable) {//TODO ? add footer with retry behavior
        isLoading = false

        mPage--

        view?.showError(ExceptionHandler.sendExceptionAndGetReadableMessage(exception))
    }

    fun loadFirstPage() {
        if (!isLoading && !allIsLoaded){
            mPage = 1
            allIsLoaded = false

            view?.clearList()
            view?.removeAllHeadersAndFooters()
            view?.showHeader()

            startLoading()
        }
    }

    fun loadNextPage() {
        if (!isLoading && !allIsLoaded) {
            mPage++

            view?.removeAllHeadersAndFooters()
            view?.showFooter()

            startLoading()
        }
    }

    fun stopLoading(){
        isLoading = false
        allIsLoaded = false
        compositeDisposable.clear()
    }
}
