package by.d1makrat.library_fm.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import by.d1makrat.library_fm.model.Track

@Dao
interface TopTracksDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTracks(tracks: List<Track>)

    @Query("SELECT * FROM TopTracks WHERE period = :period ORDER BY rank ASC LIMIT :pageSize OFFSET (:page-1)*:pageSize")
    fun getTracks(period: String, page: Int, pageSize: Int): List<Track>

    @Query("SELECT COUNT(*) FROM TopTracks WHERE period = :period")
    fun getTracksCount(period: String): String

    @Query("DELETE FROM TopTracks")
    fun deleteAllTracks()

    @Query("DELETE FROM TopTracks WHERE period=:period")
    fun deleteTracks(period: String)
}
