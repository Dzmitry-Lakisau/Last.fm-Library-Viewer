package by.d1makrat.library_fm.json

import by.d1makrat.library_fm.APIException
import by.d1makrat.library_fm.Constants.*
import by.d1makrat.library_fm.Constants.JsonConstants.*
import by.d1makrat.library_fm.model.TopTracks
import by.d1makrat.library_fm.model.Track
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.json.JSONException
import java.util.*

class TopTracksAdapter : TypeAdapter<TopTracks>() {

    private val TOPTRACKS_KEY = "toptracks"
    private val MAX_IMAGE_RESOLUTION_INDEX = 3

    private val mGson = Gson()

    @Throws(JSONException::class)
    override fun write(jsonWriter: JsonWriter, topTracks: TopTracks) {
        mGson.toJson(topTracks, TopTracks::class.java, jsonWriter)
    }

    @Throws(JSONException::class)
    override fun read(jsonReader: JsonReader): TopTracks {

        val tracks = ArrayList<Track>()

        val rootObject = mGson.getAdapter(JsonElement::class.java).read(jsonReader).asJsonObject

        if (rootObject.has("error")) throw APIException(rootObject.get("message").asString)

        val totalTracks = rootObject.get(TOPTRACKS_KEY).asJsonObject.get(ATTRIBUTE_KEY).asJsonObject.get(TOTAL_KEY).asInt
        val tracksJsonArray = rootObject.get(TOPTRACKS_KEY).asJsonObject.getAsJsonArray(TRACK_KEY)

        for (i in 0 until tracksJsonArray.size()) {
            val trackJsonObject = tracksJsonArray.get(i).asJsonObject

            val title = trackJsonObject.get(NAME_KEY).asString

            val artistName = trackJsonObject.get(ARTIST_KEY).asJsonObject.get(NAME_KEY).asString

            val playCount = trackJsonObject.get(PLAYCOUNT_KEY).asInt

            val jsonArray = trackJsonObject.get(IMAGE_KEY).asJsonArray
            var imageUrl = jsonArray.get(MAX_IMAGE_RESOLUTION_INDEX).asJsonObject.get(TEXT_KEY).asString
            imageUrl = if (imageUrl == EMPTY_STRING) null else imageUrl

            val rank = trackJsonObject.get(ATTRIBUTE_KEY).asJsonObject.get(RANK_KEY).asString

            tracks.add(Track(title, artistName, playCount, imageUrl, rank))
        }

        return TopTracks(tracks, totalTracks)
    }
}
