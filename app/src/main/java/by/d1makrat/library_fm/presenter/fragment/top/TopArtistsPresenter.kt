package by.d1makrat.library_fm.presenter.fragment.top

import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.model.Artist
import by.d1makrat.library_fm.model.Period
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TopArtistsPresenter(period: String): TopItemsPresenter<Artist>(period) {
    init {
        mUrlForBrowser = AppContext.getInstance().user.url + "/library/artists" + Period().getSuffixForUrl(period)
    }

    override fun performOperation() {
        compositeDisposable.add(
                repository.getTopArtists(period, mPage)
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
