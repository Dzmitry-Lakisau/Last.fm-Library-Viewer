package by.d1makrat.library_fm.database

import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Database
import by.d1makrat.library_fm.model.Scrobble

@Database(entities = [Scrobble::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scrobblesDao(): ScrobblesDao
}
