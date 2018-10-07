package by.d1makrat.library_fm.presenter.fragment.scrobble

import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.asynctask.GetItemsAsyncTask
import by.d1makrat.library_fm.model.Scrobble
import by.d1makrat.library_fm.operation.ScrobblesOfArtistOperation

class ScrobblesOfArtistPresenter(val artist: String, from: Long?, to: Long?): ScrobblesPresenter(from, to) {

    init {
        mUrlForBrowser = AppContext.getInstance().user.url + "/library/music/" + artist
    }

    override fun performOperation() {
        val scrobblesOfArtistOperation = ScrobblesOfArtistOperation(artist, mPage, from, to)
        val getItemsAsyncTask = GetItemsAsyncTask<Scrobble>(this)
        getItemsAsyncTask.execute(scrobblesOfArtistOperation)
    }
}
