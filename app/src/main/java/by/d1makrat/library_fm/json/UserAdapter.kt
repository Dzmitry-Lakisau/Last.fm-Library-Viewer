package by.d1makrat.library_fm.json

import by.d1makrat.library_fm.Constants.*
import by.d1makrat.library_fm.Constants.JsonConstants.IMAGE_KEY
import by.d1makrat.library_fm.Constants.JsonConstants.TEXT_KEY
import by.d1makrat.library_fm.model.User
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.json.JSONException


class UserAdapter : TypeAdapter<User>() {

    private val REGISTERED_KEY = "registered"
    private val UNIXTIME_KEY = "unixtime"
    private val MAX_IMAGE_RESOLUTION_INDEX = 3

    private val mGson = Gson()

    @Throws(JSONException::class)
    override fun write(jsonWriter: JsonWriter, user: User) {
        mGson.toJson(user, User::class.java, jsonWriter)
    }

    @Throws(JSONException::class)
    override fun read(jsonReader: JsonReader): User {

        val jsonElement = mGson.getAdapter(JsonElement::class.java).read(jsonReader)

        val userJsonObject: JsonObject = jsonElement.asJsonObject.get(USER_KEY).asJsonObject

        val username = userJsonObject.get(NAME_KEY).toString()
        val playcount = userJsonObject.get(PLAYCOUNT_KEY).toString()
        val registered = userJsonObject.getAsJsonObject(REGISTERED_KEY).get(UNIXTIME_KEY).toString()
        val url = userJsonObject.get(URL_KEY).toString()
        val avatarUrl = userJsonObject.getAsJsonArray(IMAGE_KEY).get(MAX_IMAGE_RESOLUTION_INDEX).asJsonObject.get(TEXT_KEY).toString()

        return User(username, playcount, registered, url, avatarUrl)
    }
}
