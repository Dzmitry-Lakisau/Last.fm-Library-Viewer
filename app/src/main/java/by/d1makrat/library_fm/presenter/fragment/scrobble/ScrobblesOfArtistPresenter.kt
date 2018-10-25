package by.d1makrat.library_fm.presenter.fragment.scrobble

import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.model.FilterRange
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ScrobblesOfArtistPresenter(val artist: String, filterRange: FilterRange): ScrobblesPresenter(filterRange) {

    init {
        mUrlForBrowser = AppContext.getInstance().user.url + "/library/music/" + artist
    }

    override fun performOperation() {
        compositeDisposable.add(
                repository.getScrobblesOfArtist(artist, mPage, filterRange.startOfPeriod, filterRange.endOfPeriod)
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
