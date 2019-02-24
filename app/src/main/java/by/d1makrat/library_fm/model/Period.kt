package by.d1makrat.library_fm.model

import by.d1makrat.library_fm.Constants

class Period{

    val size = 6

    fun getValueForApi(position: Int): String {
        return when(position){
            0 -> "overall"
            1 -> "7day"
            2 -> "1month"
            3 -> "3month"
            4 -> "6month"
            5 -> "12month"
            else -> "overall"
        }
    }

    fun getName(position: Int): String {
        return when(position){
            0 -> "All-time"
            1 -> "Week"
            2 -> "Month"
            3 -> "3 months"
            4 -> "6 months"
            5 -> "Year"
            else -> "Overall"
        }
    }

    fun getSuffixForUrl(paramForApi: String): String {
        return when(paramForApi){
            "overall" -> Constants.EMPTY_STRING
            "7day" -> "?date_preset=LAST_7_DAYS"
            "1month" -> "?date_preset=LAST_30_DAYS"
            "3month" -> "?date_preset=LAST_90_DAYS"
            "6month" -> "?date_preset=LAST_180_DAYS"
            "12month" -> "?date_preset=LAST_365_DAYS"
            else -> Constants.EMPTY_STRING
        }
    }

    fun getSuffixForUrl(position: Int): String {
        return when(position){
            0 -> Constants.EMPTY_STRING
            1 -> "?date_preset=LAST_7_DAYS"
            2 -> "?date_preset=LAST_30_DAYS"
            3 -> "?date_preset=LAST_90_DAYS"
            4 -> "?date_preset=LAST_180_DAYS"
            5 -> "?date_preset=LAST_365_DAYS"
            else -> Constants.EMPTY_STRING
        }
    }
}
