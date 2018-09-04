package by.d1makrat.library_fm.model

import java.util.*
import java.util.concurrent.TimeUnit

class User(val username: String, val playCount: String, private val registered: Long, val url: String, val avatarUrl: String) {

    fun getRegistered(): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = TimeUnit.SECONDS.toMillis(registered)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)
        val year = calendar.get(Calendar.YEAR)
        return "$dayOfMonth $month $year"
        }
}
