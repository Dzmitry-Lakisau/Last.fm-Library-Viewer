package by.d1makrat.library_fm.json

import by.d1makrat.library_fm.APIException
import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.R
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
        when (message) {
            "0" -> return SendScrobbleResult(AppContext.getInstance().getString(R.string.manual_fragment_scrobble_accepted), 0)
            "2" -> return SendScrobbleResult(AppContext.getInstance().getString(R.string.manual_fragment_track_ignored), 2)
            "3" -> return SendScrobbleResult(AppContext.getInstance().getString(R.string.manual_fragment_timestamp_old), 3)
            "4" -> return SendScrobbleResult(AppContext.getInstance().getString(R.string.manual_fragment_timestamp_new), 4)
            "5" -> return SendScrobbleResult(AppContext.getInstance().getString(R.string.manual_fragment_limit_exceeded), 5)
            else -> return SendScrobbleResult(AppContext.getInstance().getString(R.string.manual_fragment_ignored_message), 1)
        }
    }
}
