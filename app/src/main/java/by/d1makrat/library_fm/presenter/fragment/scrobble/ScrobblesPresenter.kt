package by.d1makrat.library_fm.presenter.fragment.scrobble

import android.net.Uri
import by.d1makrat.library_fm.model.Scrobble
import by.d1makrat.library_fm.presenter.fragment.ItemsPresenter
import by.d1makrat.library_fm.utils.DateUtils
import by.d1makrat.library_fm.view.fragment.ScrobblesView

abstract class ScrobblesPresenter(var from: Long, var to: Long): ItemsPresenter<Scrobble, ScrobblesView<Scrobble>>() {

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
        view?.openBrowser(Uri.parse(DateUtils.getUrlFromTimestamps(mUrlForBrowser, from, to)))
    }

    fun onLoadingSuccessful(items: List<Scrobble>) {
        view?.removeAllHeadersAndFooters()
        isLoading = false

        val size = items.size
        when {
            size > 0 -> {
                view?.populateList(items)
                view?.showListHead(DateUtils.getMessageFromTimestamps(view?.getListItemsCount()!!, from, to))

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

    fun onFinishFilterDialog(pFrom: Long, pTo: Long) {
        allIsLoaded = false
        from = pFrom
        to = pTo

        view?.clearList()
        view?.hideListHead()

        mPage = 0
        loadItems()
    }

    fun onCreatingNewView(){
        view?.hideListHead()
        loadItems()
    }

    fun onShowingFromBackStack(itemCount: Int){
        view?.showListHead(DateUtils.getMessageFromTimestamps(itemCount, from, to))
    }

    fun onScrobblesOfDayPressed(isRecentScrobblesFragment: Boolean, listItemPressed: Scrobble){
        if (isRecentScrobblesFragment)
            onFinishFilterDialog(DateUtils.getStartTimestampOfDay(listItemPressed.getRawDate()),
                    DateUtils.getEndTimestampOfDay(listItemPressed.getRawDate()))
        else
            view?.openScrobblesFragment(DateUtils.getStartTimestampOfDay(listItemPressed.getRawDate()),
                DateUtils.getEndTimestampOfDay(listItemPressed.getRawDate()))
    }
}
