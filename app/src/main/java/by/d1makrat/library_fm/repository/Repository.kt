package by.d1makrat.library_fm.repository

import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.database.DatabaseHelper
import by.d1makrat.library_fm.model.Scrobble
import by.d1makrat.library_fm.model.TopAlbums
import by.d1makrat.library_fm.model.TopArtists
import by.d1makrat.library_fm.model.TopTracks
import by.d1makrat.library_fm.retrofit.LastFmRestApiService
import by.d1makrat.library_fm.utils.ConnectionChecker
import io.reactivex.Completable
import io.reactivex.Single

class Repository(private val restApiWorker: LastFmRestApiService, private val databaseHelper: DatabaseHelper) {

    fun clearDatabase(): Completable {
        return Completable.create{completableEmitter ->
            try {
                databaseHelper.deleteScrobbles()
                databaseHelper.deleteTopAlbums()
                databaseHelper.deleteTopArtists()
                databaseHelper.deleteTopTracks()

                completableEmitter.onComplete()
            }
            catch (e: Exception) {
                completableEmitter.onError(e)
            }
        }
    }

    fun getScrobbles(page: Int, from: Long?, to: Long?): Single<List<Scrobble>> {
        return Single.create { singleEmitter ->
            try {
                lateinit var scrobbles: List<Scrobble>

                if (ConnectionChecker.isNetworkAvailable()) {
                    val response = restApiWorker.getRecentScrobbles(AppContext.getInstance().user.username, page, AppContext.getInstance().limit, from, to)
                            .execute()

                    if (response.isSuccessful) {
                        scrobbles = response.body()!!.getAll()
                        databaseHelper.insertScrobbles(scrobbles)
                    }
                }
                else {
                    scrobbles = databaseHelper.getScrobbles(page, from, to)
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

    fun getScrobblesOfArtist(artist: String, page: Int, from: Long?, to: Long?): Single<List<Scrobble>> {
        return Single.create { singleEmitter ->
            try {
                if (ConnectionChecker.isNetworkAvailable()) {
                    val response = restApiWorker.getScrobblesOfArtist(AppContext.getInstance().user.username, artist, page, AppContext.getInstance().limit, from, to)
                            .execute()

                    if (response.isSuccessful) {
                        val artistScrobbles = response.body()!!.getAll()
                        databaseHelper.insertScrobbles(artistScrobbles)
                    }
                }

                singleEmitter.onSuccess(databaseHelper.getScrobblesOfArtist(artist, page, from, to))
            }
            catch (e: Exception){
                if (!singleEmitter.isDisposed) {
                    singleEmitter.onError(e)
                }
            }
        }
    }

    fun getScrobblesOfAlbum(artist: String, album: String, from: Long?, to: Long?): Single<List<Scrobble>> {
        return Single.create { singleEmitter ->
            try {
                if (ConnectionChecker.isNetworkAvailable()) {
                    var page = 1
                    do {
                        val response = restApiWorker.getScrobblesOfArtist(AppContext.getInstance().user.username, artist, page, AppContext.getInstance().limit, from, to)
                                .execute()

                        lateinit var artistScrobbles: List<Scrobble>
                        if (response.isSuccessful) {
                            artistScrobbles = response.body()!!.getAll()
                            databaseHelper.insertScrobbles(artistScrobbles)
                        }

                        page++
                    }
                    while (artistScrobbles.isNotEmpty())
                }

                singleEmitter.onSuccess(databaseHelper.getScrobblesOfAlbum(artist, album, from, to))
            }
            catch (e: Exception){
                if (!singleEmitter.isDisposed) {
                    singleEmitter.onError(e)
                }
            }
        }
    }

    fun getScrobblesOfTrack(artist: String, track: String, from: Long?, to: Long?): Single<List<Scrobble>> {
        return Single.create { singleEmitter ->
            try {
                if (ConnectionChecker.isNetworkAvailable()) {
                    var page = 1
                    do {
                        val response = restApiWorker.getScrobblesOfArtist(AppContext.getInstance().user.username, artist, page, AppContext.getInstance().limit, from, to)
                                .execute()

                        lateinit var artistScrobbles: List<Scrobble>
                        if (response.isSuccessful) {
                            artistScrobbles = response.body()!!.getAll()
                            databaseHelper.insertScrobbles(artistScrobbles)
                        }

                        page++
                    }
                    while (artistScrobbles.isNotEmpty())
                }

                singleEmitter.onSuccess(databaseHelper.getScrobblesOfTrack(artist, track, from, to))
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
                            databaseHelper.deleteTopAlbums(period)
                        }
                        databaseHelper.insertTopAlbums(topAlbums.items, period)
                    }
                } else {
                    topAlbums = databaseHelper.getTopAlbums(period, page)
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
                            databaseHelper.deleteTopArtists(period)
                        }
                        databaseHelper.insertTopArtists(topArtists.items, period)
                    }
                } else {
                    topArtists = databaseHelper.getTopArtists(period, page)
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
                            databaseHelper.deleteTopTracks(period)
                        }
                        databaseHelper.insertTopTracks(topTracks.items, period)
                    }
                } else {
                    topTracks = databaseHelper.getTopTracks(period, page)
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
