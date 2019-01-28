package by.d1makrat.library_fm

import by.d1makrat.library_fm.model.SessionKey
import by.d1makrat.library_fm.utils.SharedPreferencesWorker

class Settings(private val sharedPreferencesWorker: SharedPreferencesWorker) {

    var sessionKey: SessionKey = SessionKey(null)

    var pageSize: Int = 10

    fun save(){
        sharedPreferencesWorker.writeSessionKey(sessionKey)
        sharedPreferencesWorker.writePageSize(pageSize)
    }
}
