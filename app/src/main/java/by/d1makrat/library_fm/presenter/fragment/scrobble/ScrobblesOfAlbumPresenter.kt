package by.d1makrat.library_fm.presenter.fragment.scrobble

import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.asynctask.GetItemsAsyncTask
import by.d1makrat.library_fm.model.Scrobble
import by.d1makrat.library_fm.operation.ScrobblesOfAlbumOperation

class ScrobblesOfAlbumPresenter(val artist: String, val album: String, from: Long?, to: Long?): ScrobblesPresenter(from, to) {

    init {
        mUrlForBrowser = AppContext.getInstance().user.url + "/library/music/" + artist + "/" + album
    }

    override fun performOperation() {
        val scrobblesOfAlbumOperation = ScrobblesOfAlbumOperation(artist, album, from, to)
        val getItemsAsyncTask = GetItemsAsyncTask<Scrobble>(this)
        getItemsAsyncTask.execute(scrobblesOfAlbumOperation)
    }

    override fun checkIfAllIsLoaded(size: Int) {
        allIsLoaded = true
        view?.showAllIsLoaded()
    }
}
