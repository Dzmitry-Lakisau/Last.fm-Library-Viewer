package by.d1makrat.library_fm.json

import by.d1makrat.library_fm.Constants.PLAYCOUNT_KEY
import by.d1makrat.library_fm.Constants.URL_KEY
import by.d1makrat.library_fm.model.User
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.json.JSONException

class UserSharedPreferencesAdapter : TypeAdapter<User>() {

    private val USERNAME_KEY = "username"
    private val REGISTERED_KEY = "registered"
    private val AVATAR_KEY = "avatarUrl"

    private val mGson = Gson()

    @Throws(JSONException::class)
    override fun write(jsonWriter: JsonWriter?, user: User?) {
        mGson.toJson(user, User::class.java, jsonWriter)
    }

    @Throws(JSONException::class)
    override fun read(jsonReader: JsonReader): User? {

    val jsonObject = mGson.getAdapter(JsonElement::class.java).read(jsonReader).asJsonObject

    return if (jsonObject.has(USERNAME_KEY) && jsonObject.has(PLAYCOUNT_KEY) && jsonObject.has(REGISTERED_KEY) &&
            jsonObject.has(URL_KEY) && jsonObject.has(AVATAR_KEY)) {
        User(jsonObject.get(USERNAME_KEY).asString, jsonObject.get(PLAYCOUNT_KEY).asString, jsonObject.get(REGISTERED_KEY).asString,
                jsonObject.get(URL_KEY).asString, jsonObject.get(AVATAR_KEY).asString)
    }
    else {
        null
    }
    }
}
