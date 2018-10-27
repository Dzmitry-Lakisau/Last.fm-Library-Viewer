package by.d1makrat.library_fm.model

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class Scrobble(val artist: String, val trackTitle: String, val album: String? = null, val imageUrl: String? = null, private val unixDate: Long){
    fun getFormattedDate(): String {
        val date = Date(TimeUnit.SECONDS.toMillis(unixDate))
        val simpleDateFormat = SimpleDateFormat("d MMM yyyy, HH:mm:ss, EEEE", Locale.ENGLISH)
        return simpleDateFormat.format(date)
    }

    fun getRawDate(): Long {
        return unixDate
    }
}
