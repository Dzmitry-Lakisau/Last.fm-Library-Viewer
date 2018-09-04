package by.d1makrat.library_fm.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@Entity(tableName = "Scrobbles", indices = [(Index(value=["artist", "track", "date"], unique = true))])
open class Scrobble {

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    var artist: String? = null

    @ColumnInfo(name = "track")
    var trackTitle: String? = null

    var album: String? = null

    var imageUri: String? = null

    @ColumnInfo(name = "date")
    var unixDate: Long? = null

    fun getFormattedDate(): String {
        val date = java.util.Date(TimeUnit.SECONDS.toMillis(unixDate as Long))
        val sdf = SimpleDateFormat("d MMM yyyy, HH:mm:ss, EEEE", Locale.ENGLISH)
        return sdf.format(date)
    }

    fun getRawDate(): Long? {
        return unixDate
    }
}
