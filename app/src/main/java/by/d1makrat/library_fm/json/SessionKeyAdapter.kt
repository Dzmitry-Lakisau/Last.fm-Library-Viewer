package by.d1makrat.library_fm.json

import by.d1makrat.library_fm.APIException
import by.d1makrat.library_fm.model.SessionKey
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.json.JSONException

class SessionKeyAdapter : TypeAdapter<SessionKey>() {

    private val mGson = Gson()

    @Throws(JSONException::class)
    override fun write(jsonWriter: JsonWriter, sessionKey: SessionKey) {
        mGson.toJson(sessionKey, SessionKey::class.java, jsonWriter)
    }

    @Throws(JSONException::class)
    override fun read(jsonReader: JsonReader): SessionKey {
        val rootObject = mGson.getAdapter(JsonElement::class.java).read(jsonReader).asJsonObject

        if (rootObject.has("error")) throw APIException(rootObject.get("message").asString)
        else return SessionKey(rootObject.get("session").asJsonObject.get("key").asString)
    }
}
