package by.d1makrat.library_fm.utils

import android.content.Context
import android.preference.PreferenceManager
import by.d1makrat.library_fm.Constants.USER_KEY
import by.d1makrat.library_fm.model.SessionKey
import by.d1makrat.library_fm.model.User
import com.google.gson.Gson

class SharedPreferencesWorker(val context: Context, private val gson: Gson) {

    private val SCROBBLES_PER_PAGE_KEY = "scrobbles_per_page"
    private val SESSIONKEY_KEY = "session_key"
    private val DEFAULT_PAGE_SIZE = 10

    private val mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun readSessionKey(): SessionKey {
        return SessionKey(mSharedPreferences.getString(SESSIONKEY_KEY, null))
    }

    fun readPageSize(): Int {
        return mSharedPreferences.getInt(SCROBBLES_PER_PAGE_KEY, DEFAULT_PAGE_SIZE)
    }

    fun readUser(): User {
        val userSharedPreferences = mSharedPreferences.getString(USER_KEY, null)
        return gson.fromJson(userSharedPreferences, User::class.java)
    }

    fun writeSessionKey(sessionKey: SessionKey){
        val editor = mSharedPreferences.edit()
        editor.putString(SESSIONKEY_KEY, sessionKey.key)
        editor.apply()
    }

    fun writePageSize(pageSize: Int){
        val editor = mSharedPreferences.edit()
        editor.putInt(SESSIONKEY_KEY, pageSize)
        editor.apply()
    }
}
