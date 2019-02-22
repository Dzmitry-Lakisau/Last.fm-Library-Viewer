package by.d1makrat.library_fm.presenter.fragment.scrobble

import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.model.FilterRange
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ScrobblesOfTrackPresenter(val artist: String, val track: String, filterRange: FilterRange): ScrobblesPresenter(filterRange) {

    init {
        mUrlForBrowser = AppContext.getInstance().user.url + "/library/music/" + artist + "/_/" + track
    }

    override fun startLoading() {
        isLoading = true

        compositeDisposable.add(
                repository.getScrobblesOfTrack(artist, track, filterRange.startOfPeriod, filterRange.endOfPeriod)
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

    override fun checkIfAllIsLoaded(size: Int) {
        allIsLoaded = true
        view?.showAllIsLoaded()
    }
}
