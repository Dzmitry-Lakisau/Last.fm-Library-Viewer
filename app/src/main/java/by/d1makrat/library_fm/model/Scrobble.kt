package by.d1makrat.library_fm.model

import android.arch.persistence.room.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@Entity(tableName = "Scrobbles", indices = [(Index(value=["artist", "track", "date"], unique = true))])
data class Scrobble(

    @PrimaryKey
    val id: Long?,

    @ColumnInfo(name = "track")
    val trackTitle: String,

    val artist: String,

    val album: String?,

    @ColumnInfo(name = "date")
    val unixDate: Long,

    val imageUri: String?
){
    @Ignore
    constructor(trackTitle: String, artist: String, album: String?, unixDate: Long, imageUri: String?):
            this(null, trackTitle, artist, album, unixDate, imageUri)

    fun getFormattedDate(): String {
        val date = java.util.Date(TimeUnit.SECONDS.toMillis(unixDate))
        val simpleDateFormat = SimpleDateFormat("d MMM yyyy, HH:mm:ss, EEEE", Locale.ENGLISH)
        return simpleDateFormat.format(date)
    }

    fun getRawDate(): Long {
        return unixDate
    }
}
