package by.d1makrat.library_fm.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import by.d1makrat.library_fm.model.Artist

@Dao
interface TopArtistsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertArtists(artist: List<Artist>)

    @Query("SELECT * FROM TopArtists WHERE period = :period ORDER BY rank ASC LIMIT :pageSize OFFSET (:page-1)*:pageSize")
    fun getArtists(period: String, page: Int, pageSize: Int): List<Artist>

    @Query("SELECT COUNT(*) FROM TopArtists WHERE period = :period")
    fun getArtistsCount(period: String): String

    @Query("DELETE FROM TopArtists")
    fun deleteAllArtists()

    @Query("DELETE FROM TopArtists WHERE period=:period")
    fun deleteArtists(period: String)
}
