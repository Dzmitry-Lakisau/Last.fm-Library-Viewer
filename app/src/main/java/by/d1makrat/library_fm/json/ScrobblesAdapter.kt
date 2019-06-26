package by.d1makrat.library_fm.json

import by.d1makrat.library_fm.APIException
import by.d1makrat.library_fm.Constants.*
import by.d1makrat.library_fm.Constants.JsonConstants.*
import by.d1makrat.library_fm.json.model.ScrobblesJsonModel
import by.d1makrat.library_fm.model.Scrobble
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.json.JSONException

class ScrobblesAdapter : TypeAdapter<ScrobblesJsonModel>() {

    private val RECENT_TRACKS_KEY = "recenttracks"
    private val ARTIST_TRACKS_KEY = "artisttracks"
    private val TRACK_SCROBBLES_KEY = "trackscrobbles"
    private val MAX_IMAGE_RESOLUTION_INDEX = 3
    private val STAR_IMAGE_URL = "https://lastfm-img2.akamaized.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.png"

    private val mGson = Gson()

    @Throws(JSONException::class)
    override fun write(jsonWriter: JsonWriter, scrobblesJsonModel: ScrobblesJsonModel) {
        mGson.toJson(scrobblesJsonModel, ScrobblesJsonModel::class.java, jsonWriter)
    }

    @Throws(JSONException::class)
    override fun read(jsonReader: JsonReader): ScrobblesJsonModel {
        val scrobbles = ScrobblesJsonModel()

        val rootObject = mGson.getAdapter(JsonElement::class.java).read(jsonReader).asJsonObject

        if (rootObject.has("error")) throw APIException(rootObject.get("message").asString)

        val scrobblesJsonArray = when {
            rootObject.has(RECENT_TRACKS_KEY) -> rootObject.get(RECENT_TRACKS_KEY).asJsonObject.get(TRACK_KEY).asJsonArray
            rootObject.has(TRACK_SCROBBLES_KEY) -> rootObject.get(TRACK_SCROBBLES_KEY).asJsonObject.get(TRACK_KEY).asJsonArray
            else -> rootObject.get(ARTIST_TRACKS_KEY).asJsonObject.get(TRACK_KEY).asJsonArray
        }

        for (i in 0 until scrobblesJsonArray.size()) {
            val scrobbleJsonObject = scrobblesJsonArray.get(i).asJsonObject

            if (!scrobbleJsonObject.has(ATTRIBUTE_KEY)) {//TODO parse and show scrobble that "now playing"
                val trackTitle = scrobbleJsonObject.get(NAME_KEY).asString
                val artist = scrobbleJsonObject.get(ARTIST_KEY).asJsonObject.get(TEXT_KEY).asString
                val album = scrobbleJsonObject.get(ALBUM_KEY).asJsonObject.get(TEXT_KEY).asString
                var imageUri = scrobbleJsonObject.get(IMAGE_KEY).asJsonArray.get(MAX_IMAGE_RESOLUTION_INDEX).asJsonObject.get(TEXT_KEY).asString
                if (imageUri == STAR_IMAGE_URL) imageUri = null
                val unixDate = scrobbleJsonObject.get(DATE_KEY).asJsonObject.get("uts").asLong

                scrobbles.add(Scrobble(artist, trackTitle, album, imageUri, unixDate))
            }
        }

        return scrobbles
    }
}
