package by.d1makrat.library_fm.model

import java.util.*
import java.util.concurrent.TimeUnit

class User(val username: String, val playcount: String, _registered: String, val url: String, val avatarUrl: String){
    val registered: String = _registered
        get() {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = TimeUnit.SECONDS.toMillis(java.lang.Long.valueOf(field))
            return calendar.get(Calendar.DAY_OF_MONTH).toString() + " " + calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH) + " " + calendar.get(Calendar.YEAR)
        }
}
