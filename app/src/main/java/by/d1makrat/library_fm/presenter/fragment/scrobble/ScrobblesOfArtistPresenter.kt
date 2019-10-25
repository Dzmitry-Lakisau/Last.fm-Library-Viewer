package by.d1makrat.library_fm.presenter.fragment.scrobble

import android.util.Log
import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.Constants.MAX_FOR_SCROBBLES_BY_ARTIST
import by.d1makrat.library_fm.model.FilterRange
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ScrobblesOfArtistPresenter(val artist: String, filterRange: FilterRange): ScrobblesPresenter(filterRange) {

    init {
        mUrlForBrowser = AppContext.getInstance().user.url + "/library/music/" + artist
    }

    override fun startLoading() {
        isLoading = true

        val start = System.currentTimeMillis()

        compositeDisposable.add(
                repository.getScrobblesOfArtist(artist, filterRange.startOfPeriod, filterRange.endOfPeriod)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {
                                    onLoadingSuccessful(it)

                                    Log.e(this.toString(), (System.currentTimeMillis() - start).toString())
                                },
                                {
                                    onException(it)

                                    Log.e(this.toString(), (System.currentTimeMillis() - start).toString())
                                }
                        )
        )
    }

    override fun checkIfAllIsLoaded(size: Int) {
        if (size < AppContext.getInstance().limit && size < MAX_FOR_SCROBBLES_BY_ARTIST){
            allIsLoaded = true
            view?.showAllIsLoaded()
        }
    }
}
