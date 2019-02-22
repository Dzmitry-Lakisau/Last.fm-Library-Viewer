package by.d1makrat.library_fm.presenter.fragment.top

import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.model.Album
import by.d1makrat.library_fm.model.Period
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TopAlbumsPresenter(period: String): TopItemsPresenter<Album>(period) {
    init {
        mUrlForBrowser = AppContext.getInstance().user.url + "/library/albums" + Period().getSuffixForUrl(period)
    }

    override fun startLoading() {
        isLoading = true

        compositeDisposable.add(
                repository.getTopAlbums(period, mPage)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {
                                    onLoadingSuccessful(it)
                                },
                                {
                                    onException(it)
                                }
                        )
        )
    }
}
