package by.d1makrat.library_fm.presenter.fragment.scrobble

import android.net.Uri
import by.d1makrat.library_fm.asynctask.GetItemsCallback
import by.d1makrat.library_fm.model.Scrobble
import by.d1makrat.library_fm.presenter.fragment.ItemsPresenter
import by.d1makrat.library_fm.utils.DateUtils
import by.d1makrat.library_fm.view.ScrobblesView

abstract class ScrobblesPresenter(var from: Long?, var to: Long?): ItemsPresenter<Scrobble, ScrobblesView<Scrobble>>(), GetItemsCallback<Scrobble> {

    fun showListHead(itemCount: Int) {
        view?.showListHead(DateUtils.getMessageFromTimestamps(itemCount, from, to))
    }

    fun onRefresh(){
        if (!isLoading) {
            allIsLoaded = false

            view?.clearList()
            view?.hideListHead()

            mPage = 0
            loadItems()
        }
    }

    fun onFilter(){
        if (!isLoading) {
            view?.showFilterDialog()
        }
    }

    fun onOpenInBrowser() {
        view?.openBrowser(
                if (from != null && to != null) {
                    Uri.parse(DateUtils.getUrlFromTimestamps(mUrlForBrowser, from, to))
                }
                else {
                    Uri.parse(mUrlForBrowser)
                }
        )
    }

    override fun onLoadingSuccessful(items: List<Scrobble>) {
        view?.removeAllHeadersAndFooters()

        val size = items.size
        when {
            size > 0 -> {
                view?.populateList(items)
                showListHead(view?.getListItemsCount()!!)

                checkIfAllIsLoaded(size)
            }
            view?.getListItemsCount() == 0 -> view?.showEmptyHeader(DateUtils.getMessageFromTimestamps(view?.getListItemsCount() as Int, from, to))
            else -> checkIfAllIsLoaded(size)
        }

        //hack when recyclerView not showing items after first load when loaded items take place that less than screen size
//        if (mLayoutManager.getChildCount() == mLayoutManager.getItemCount()) {
//            mPage++
//            loadItems()
//        }
    }

    fun onFinishFilterDialog(pFrom: Long?, pTo: Long?) {
        allIsLoaded = false
        from = pFrom
        to = pTo

        view?.clearList()
        showListHead(view?.getListItemsCount() as Int)

        mPage = 1
        loadItems()
    }


}
