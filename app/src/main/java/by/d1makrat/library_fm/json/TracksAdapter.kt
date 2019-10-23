package by.d1makrat.library_fm.json

import by.d1makrat.library_fm.APIException
import by.d1makrat.library_fm.Constants
import by.d1makrat.library_fm.model.TopTrack
import by.d1makrat.library_fm.model.TopTracks
import by.d1makrat.library_fm.model.Track
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.json.JSONException
import java.util.ArrayList

class TracksAdapter : TypeAdapter<List<Track>>() {

    private val TOPTRACKS_KEY = "toptracks"

    private val mGson = Gson()

    @Throws(JSONException::class)
    override fun write(jsonWriter: JsonWriter, topTracks: List<Track>) {
        mGson.toJson(topTracks, object : TypeToken<List<Track>>(){}.type, jsonWriter)
    }

    @Throws(JSONException::class)
    override fun read(jsonReader: JsonReader): List<Track> {

        val tracks = ArrayList<Track>()

        val rootObject = mGson.getAdapter(JsonElement::class.java).read(jsonReader).asJsonObject

        if (rootObject.has("error")) throw APIException(rootObject.get("message").asString)

        val tracksJsonArray = rootObject.get(TOPTRACKS_KEY).asJsonObject.getAsJsonArray(Constants.TRACK_KEY)

        for (i in 0 until tracksJsonArray.size()) {
            val trackJsonObject = tracksJsonArray.get(i).asJsonObject

            val title = trackJsonObject.get(Constants.NAME_KEY).asString

            val artistName = trackJsonObject.get(Constants.ARTIST_KEY).asJsonObject.get(Constants.NAME_KEY).asString

            val playCount = trackJsonObject.get(Constants.PLAYCOUNT_KEY).asInt

            tracks.add(Track(artistName, title, playCount))
        }

        return tracks
    }
}