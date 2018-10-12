package by.d1makrat.library_fm.presenter.fragment.scrobble

import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.asynctask.GetItemsAsyncTask
import by.d1makrat.library_fm.model.Scrobble
import by.d1makrat.library_fm.operation.ScrobblesOfTrackOperation

class ScrobblesOfTrackPresenter(val artist: String, val track: String, from: Long, to: Long): ScrobblesPresenter(from, to) {

    init {
        mUrlForBrowser = AppContext.getInstance().user.url + "/library/music/" + artist + "/_/" + track
    }

    override fun performOperation() {
        val scrobblesOfTrackOperation = ScrobblesOfTrackOperation(artist, track, from, to)
        val getItemsAsyncTask = GetItemsAsyncTask<Scrobble>(this)
        getItemsAsyncTask.execute(scrobblesOfTrackOperation)
    }

    override fun checkIfAllIsLoaded(size: Int) {
        allIsLoaded = true
        view?.showAllIsLoaded()
    }
}
