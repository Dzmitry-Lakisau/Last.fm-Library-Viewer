package by.d1makrat.library_fm.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import by.d1makrat.library_fm.model.Album

@Dao
interface TopAlbumsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAlbums(albums: List<Album>)

    @Query("SELECT * FROM TopAlbums WHERE period = :period ORDER BY rank ASC LIMIT :pageSize OFFSET (:page-1)*:pageSize")
    fun getAlbums(period: String, page: Int, pageSize: Int): List<Album>

    @Query("SELECT COUNT(*) FROM TopAlbums WHERE period = :period")
    fun getAlbumsCount(period: String): String

    @Query("DELETE FROM TopAlbums")
    fun deleteAllAlbums()

    @Query("DELETE FROM TopAlbums WHERE period=:period")
    fun deleteAlbums(period: String)
}
