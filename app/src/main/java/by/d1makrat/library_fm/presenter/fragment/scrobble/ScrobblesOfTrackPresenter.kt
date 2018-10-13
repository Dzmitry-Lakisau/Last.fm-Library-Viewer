package by.d1makrat.library_fm.presenter.fragment.scrobble

import by.d1makrat.library_fm.AppContext
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ScrobblesOfTrackPresenter(val artist: String, val track: String, from: Long, to: Long): ScrobblesPresenter(from, to) {

    init {
        mUrlForBrowser = AppContext.getInstance().user.url + "/library/music/" + artist + "/_/" + track
    }

    override fun performOperation() {
        compositeDisposable.add(
                repository.getScrobblesOfTrack(artist, track, from, to)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {
                                    onLoadingSuccessful(it)
                                },
                                {
                                    onException(Exception(it))
                                }
                        )
        )
    }

    override fun checkIfAllIsLoaded(size: Int) {
        allIsLoaded = true
        view?.showAllIsLoaded()
    }
}
