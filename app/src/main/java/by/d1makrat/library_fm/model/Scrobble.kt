package by.d1makrat.library_fm.model

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class Scrobble {
    lateinit var Artist: String
    lateinit var TrackTitle: String
    var Album: String? = null
    var ImageUrl: String? = null
    private var UnixDate: Long = 0

    fun setDate(unixDate: Long){
        UnixDate = unixDate
    }

    fun getFormattedDate(): String {
        val date = java.util.Date(TimeUnit.SECONDS.toMillis(UnixDate))
        val sdf = SimpleDateFormat("d MMM yyyy, HH:mm:ss, EEEE", Locale.ENGLISH)
        return sdf.format(date)
    }

    fun getRawDate(): Long {
        return UnixDate
    }
}
