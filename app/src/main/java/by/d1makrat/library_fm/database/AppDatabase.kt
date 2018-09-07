package by.d1makrat.library_fm.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import by.d1makrat.library_fm.model.Album
import by.d1makrat.library_fm.model.Artist
import by.d1makrat.library_fm.model.Scrobble
import by.d1makrat.library_fm.model.Track

@Database(entities = [Scrobble::class, Album::class, Artist::class, Track::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scrobblesDao(): ScrobblesDao
    abstract fun topAlbumsDao(): TopAlbumsDao
    abstract fun topArtistsDao(): TopArtistsDao
    abstract fun topTracksDao(): TopTracksDao
}
