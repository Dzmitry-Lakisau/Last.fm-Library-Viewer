package by.d1makrat.library_fm.repository

import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.Constants.DATE_LONG_DEFAULT_VALUE
import by.d1makrat.library_fm.database.DatabaseWorker
import by.d1makrat.library_fm.model.Scrobble
import by.d1makrat.library_fm.model.TopAlbums
import by.d1makrat.library_fm.model.TopArtists
import by.d1makrat.library_fm.model.TopTracks
import by.d1makrat.library_fm.retrofit.LastFmRestApiService
import by.d1makrat.library_fm.utils.ConnectionChecker
import io.reactivex.Single

class Repository(private val restApiWorker: LastFmRestApiService, private val databaseWorker: DatabaseWorker) {

    fun getScrobbles(page: Int, from: Long, to: Long): Single<List<Scrobble>> {
        return Single.create { singleEmitter ->
            try {
                lateinit var scrobbles: List<Scrobble>

                if (ConnectionChecker.isNetworkAvailable()) {
                    val response = restApiWorker.getRecentScrobbles(AppContext.getInstance().user.username, page, AppContext.getInstance().limit,
                            if (from == DATE_LONG_DEFAULT_VALUE) null else from, if (to == DATE_LONG_DEFAULT_VALUE) null else to)
                            .execute()

                    if (response.isSuccessful) {
                        scrobbles = response.body()!!.getAll()
                        databaseWorker.scrobblesTable.insertScrobbles(scrobbles)
                    }
                }
                else {
                    scrobbles = databaseWorker.scrobblesTable.getScrobbles(page, from, to)
                }

                singleEmitter.onSuccess(scrobbles)
            }
            catch (e: Exception){
                if (!singleEmitter.isDisposed) {
                    singleEmitter.onError(e)
                }
            }
        }
    }

    fun getScrobblesOfArtist(artist: String, page: Int, from: Long, to: Long): Single<List<Scrobble>> {
        return Single.create { singleEmitter ->
            try {
                if (ConnectionChecker.isNetworkAvailable()) {
                    val response = restApiWorker.getScrobblesOfArtist(AppContext.getInstance().user.username, artist, page, AppContext.getInstance().limit,
                            if (from == DATE_LONG_DEFAULT_VALUE) null else from, if (to == DATE_LONG_DEFAULT_VALUE) null else to)
                            .execute()

                    if (response.isSuccessful) {
                        val artistScrobbles = response.body()!!.getAll()
                        databaseWorker.scrobblesTable.insertScrobbles(artistScrobbles)
                    }
                }

                singleEmitter.onSuccess(databaseWorker.scrobblesTable.getScrobblesOfArtist(artist, page, from, to))
            }
            catch (e: Exception){
                if (!singleEmitter.isDisposed) {
                    singleEmitter.onError(e)
                }
            }
        }
    }

    fun getScrobblesOfAlbum(artist: String, album: String, from: Long, to: Long): Single<List<Scrobble>> {
        return Single.create { singleEmitter ->
            try {
                if (ConnectionChecker.isNetworkAvailable()) {
                    var page = 1
                    do {
                        val response = restApiWorker.getScrobblesOfArtist(AppContext.getInstance().user.username, artist, page, AppContext.getInstance().limit,
                                if (from == DATE_LONG_DEFAULT_VALUE) null else from, if (to == DATE_LONG_DEFAULT_VALUE) null else to)
                                .execute()

                        lateinit var artistScrobbles: List<Scrobble>
                        if (response.isSuccessful) {
                            artistScrobbles = response.body()!!.getAll()
                            databaseWorker.scrobblesTable.insertScrobbles(artistScrobbles)
                        }

                        page++
                    }
                    while (artistScrobbles.isNotEmpty())
                }

                singleEmitter.onSuccess(databaseWorker.scrobblesTable.getScrobblesOfAlbum(artist, album, from, to))
            }
            catch (e: Exception){
                if (!singleEmitter.isDisposed) {
                    singleEmitter.onError(e)
                }
            }
        }
    }

    fun getScrobblesOfTrack(artist: String, track: String, from: Long, to: Long): Single<List<Scrobble>> {
        return Single.create { singleEmitter ->
            try {
                if (ConnectionChecker.isNetworkAvailable()) {
                    var page = 1
                    do {
                        val response = restApiWorker.getScrobblesOfArtist(AppContext.getInstance().user.username, artist, page, AppContext.getInstance().limit,
                                if (from == DATE_LONG_DEFAULT_VALUE) null else from, if (to == DATE_LONG_DEFAULT_VALUE) null else to)
                                .execute()

                        lateinit var artistScrobbles: List<Scrobble>
                        if (response.isSuccessful) {
                            artistScrobbles = response.body()!!.getAll()
                            databaseWorker.scrobblesTable.insertScrobbles(artistScrobbles)
                        }

                        page++
                    }
                    while (artistScrobbles.isNotEmpty())
                }

                singleEmitter.onSuccess(databaseWorker.scrobblesTable.getScrobblesOfTrack(artist, track, from, to))
            }
            catch (e: Exception){
                if (!singleEmitter.isDisposed) {
                    singleEmitter.onError(e)
                }
            }
        }
    }

    fun getTopAlbums(period: String, page: Int): Single<TopAlbums> {
        return Single.create { singleEmitter ->
            try {
                lateinit var topAlbums: TopAlbums

                if (ConnectionChecker.isNetworkAvailable()) {

                    val response = restApiWorker.getTopAlbums(AppContext.getInstance().user.username, period, page, AppContext.getInstance().limit).execute()

                    if (response.isSuccessful) {
                        topAlbums = response.body()!!
                        if (page == 1) {
                            databaseWorker.deleteTopAlbums(period)
                        }
                        databaseWorker.topAlbumsTable.insertTopAlbums(topAlbums.items, period)
                    }
                } else {
                    topAlbums = databaseWorker.topAlbumsTable.getTopAlbums(period, page)
                }

                singleEmitter.onSuccess(topAlbums)
            }
            catch (e: Exception){
                if (!singleEmitter.isDisposed) {
                    singleEmitter.onError(e)
                }
            }
        }
    }

    fun getTopArtists(period: String, page: Int): Single<TopArtists> {
        return Single.create { singleEmitter ->
            try {
                lateinit var topArtists: TopArtists

                if (ConnectionChecker.isNetworkAvailable()) {

                    val response = restApiWorker.getTopArtists(AppContext.getInstance().user.username, period, page, AppContext.getInstance().limit).execute()

                    if (response.isSuccessful) {
                        topArtists = response.body()!!
                        if (page == 1) {
                            databaseWorker.deleteTopArtists(period)
                        }
                        databaseWorker.topArtistsTable.insertTopArtists(topArtists.items, period)
                    }
                } else {
                    topArtists = databaseWorker.topArtistsTable.getTopArtists(period, page)
                }

                singleEmitter.onSuccess(topArtists)
            }
            catch (e: Exception){
                if (!singleEmitter.isDisposed) {
                    singleEmitter.onError(e)
                }
            }
        }
    }

    fun getTopTracks(period: String, page: Int): Single<TopTracks> {
        return Single.create { singleEmitter ->
            try {
                lateinit var topTracks: TopTracks

                if (ConnectionChecker.isNetworkAvailable()) {

                    val response = restApiWorker.getTopTracks(AppContext.getInstance().user.username, period, page, AppContext.getInstance().limit).execute()

                    if (response.isSuccessful) {
                        topTracks = response.body()!!
                        if (page == 1) {
                            databaseWorker.deleteTopTracks(period)
                        }
                        databaseWorker.topTracksTable.bulkInsertTopTracks(topTracks.items, period)
                    }
                } else {
                    topTracks = databaseWorker.topTracksTable.getTopTracks(period, page)
                }

                singleEmitter.onSuccess(topTracks)
            }
            catch (e: Exception){
                if (!singleEmitter.isDisposed) {
                    singleEmitter.onError(e)
                }
            }
        }
    }
}
