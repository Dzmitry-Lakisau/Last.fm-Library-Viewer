package by.d1makrat.library_fm.presenter.activity

import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.Constants.API_MAX_FOR_SCROBBLES_BY_ARTIST
import by.d1makrat.library_fm.database.DatabaseWorker
import by.d1makrat.library_fm.image_loader.Malevich
import by.d1makrat.library_fm.view.activity.PreferenceView
import com.google.firebase.crash.FirebaseCrash
import java.io.IOException
import java.sql.SQLException

class PreferencePresenter {

    private val MAX_ITEMS_PER_REQUEST = 1000

    private var view: PreferenceView? = null

    fun attachView(view: PreferenceView){
        this.view = view
    }

    fun detachView(){
        view = null
    }

    fun onSetLimitButtonClicked(input: String) {
        try {
            val limit = input.toInt()
            if (limit in 1..MAX_ITEMS_PER_REQUEST) {
                if (limit > API_MAX_FOR_SCROBBLES_BY_ARTIST) {
                    view?.showLimitMoreThan200Message()
                }
                AppContext.getInstance().setLimit(limit.toString())
                view?.showLimitSetOKMessage()
            } else {
                view?.showLimitMustBeBetween()
            }
        } catch (exception: NumberFormatException){
            exception.printStackTrace()
            FirebaseCrash.report(exception)
            view?.showNonnumericalInputMessage()
        }
    }

    fun onClearImageCacheButtonClicked() {
        try {
            Malevich.INSTANCE.clearCache()
            view?.showOK()
        } catch (exception: IOException) {
            exception.printStackTrace()
            FirebaseCrash.report(exception)
            view?.showUnableToClearImageCache()
        }
    }

    fun onDropDatabaseButtonClicked() {
        val databaseWorker = DatabaseWorker()

        try {
            databaseWorker.openDatabase()
            databaseWorker.deleteScrobbles()
            databaseWorker.deleteTopAlbums(null)
            databaseWorker.deleteTopArtists(null)
            databaseWorker.deleteTopTracks(null)

            view?.showOK()
        } catch (exception: SQLException) {
            exception.printStackTrace()
            FirebaseCrash.report(exception)
            view?.showUnableToDropDatabaseMessage()
        } finally {
            databaseWorker.closeDatabase()
        }
    }
}
