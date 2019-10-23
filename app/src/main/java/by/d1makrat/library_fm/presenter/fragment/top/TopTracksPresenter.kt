package by.d1makrat.library_fm.presenter.fragment.top

import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.model.Period
import by.d1makrat.library_fm.model.TopTrack
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TopTracksPresenter(period: String): TopItemsPresenter<TopTrack>(period) {
    init {
        mUrlForBrowser = AppContext.getInstance().user.url + "/library/tracks" + Period().getSuffixForUrl(period)
    }

    override fun startLoading() {
        isLoading = true

        compositeDisposable.add(
                repository.getTopTracks(period, mPage)
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
