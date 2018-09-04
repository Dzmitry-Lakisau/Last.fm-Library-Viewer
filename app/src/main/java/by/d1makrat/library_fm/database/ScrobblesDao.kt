package by.d1makrat.library_fm.database

import android.arch.persistence.room.*
import by.d1makrat.library_fm.model.Scrobble

@Dao
interface ScrobblesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(scrobbles: List<Scrobble>)

    @Query("SELECT * FROM Scrobbles ORDER BY date DESC LIMIT :pageSize OFFSET (:page-1)*:pageSize")
    fun getScrobbles(page: Int, pageSize: Int): List<Scrobble>

    @Query("SELECT * FROM Scrobbles WHERE date BETWEEN :startOfPeriod AND :endOfPeriod ORDER BY date DESC LIMIT :pageSize OFFSET (:page-1)*:pageSize")
    fun getScrobbles(startOfPeriod: Long, endOfPeriod: Long, page: Int, pageSize: Int): List<Scrobble>

    @Query("SELECT * FROM Scrobbles WHERE artist = :artist AND album = :album ORDER BY date DESC")
    fun getScrobblesOfAlbum(artist: String, album: String): List<Scrobble>

    @Query("SELECT * FROM Scrobbles WHERE artist = :artist AND album = :album AND date BETWEEN :startOfPeriod AND :endOfPeriod ORDER BY date DESC")
    fun getScrobblesOfAlbum(artist: String, album: String, startOfPeriod: Long, endOfPeriod: Long): List<Scrobble>

    @Query("SELECT * FROM Scrobbles WHERE artist = :artist ORDER BY date DESC LIMIT :pageSize OFFSET (:page-1)*:pageSize")
    fun getScrobblesOfArtist(artist: String, page: Int, pageSize: Int): List<Scrobble>

    @Query("SELECT * FROM Scrobbles WHERE artist = :artist AND date BETWEEN :startOfPeriod AND :endOfPeriod ORDER BY date DESC LIMIT :pageSize OFFSET (:page-1)*:pageSize")
    fun getScrobblesOfArtist(artist: String, startOfPeriod: Long, endOfPeriod: Long, page: Int, pageSize: Int): List<Scrobble>

    @Query("SELECT * FROM Scrobbles WHERE artist = :artist AND track = :track ORDER BY date DESC")
    fun getScrobblesOfTrack(artist: String, track: String): List<Scrobble>

    @Query("SELECT * FROM Scrobbles WHERE artist = :artist AND track = :track AND date BETWEEN :startOfPeriod AND :endOfPeriod ORDER BY date DESC")
    fun getScrobblesOfTrack(artist: String, track: String, startOfPeriod: Long, endOfPeriod: Long): List<Scrobble>

    @Query("DELETE FROM Scrobbles")
    fun deleteAllScrobbles()
}
