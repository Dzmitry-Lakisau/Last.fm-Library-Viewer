package by.d1makrat.library_fm.json

import by.d1makrat.library_fm.APIException
import by.d1makrat.library_fm.model.SendScrobbleResult
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class SendScrobbleResultAdapter : TypeAdapter<SendScrobbleResult>() {

    private val mGson = Gson()

    override fun write(jsonWriter: JsonWriter, sendScrobbleResult: SendScrobbleResult) {
        mGson.toJson(sendScrobbleResult, SendScrobbleResult::class.java, jsonWriter)
    }

    override fun read(jsonReader: JsonReader): SendScrobbleResult {
        val rootObject = mGson.getAdapter(JsonElement::class.java).read(jsonReader).asJsonObject

        if (rootObject.has("error")) throw APIException(rootObject.get("message").asString)

        val messageJsonObject = rootObject.get("scrobbles").asJsonObject.get("scrobble").asJsonObject.get("ignoredMessage").asJsonObject
        val message = messageJsonObject.get("code").asString
        return when (message) {
            "0" -> SendScrobbleResult("Scrobble was accepted", 0)
            "2" -> SendScrobbleResult("Track was ignored", 2)
            "3" -> SendScrobbleResult("Timestamp was too old", 3)
            "4" -> SendScrobbleResult("Timestamp was too new", 4)
            "5" -> SendScrobbleResult("Daily scrobble limit exceeded", 5)
            else -> SendScrobbleResult("Scrobble was ignored. Probably because of timestamp was too old or new", 1)
        }
    }
}
