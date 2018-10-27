package by.d1makrat.library_fm.json

import by.d1makrat.library_fm.APIException
import by.d1makrat.library_fm.Constants.*
import by.d1makrat.library_fm.Constants.JsonConstants.*
import by.d1makrat.library_fm.model.Artist
import by.d1makrat.library_fm.model.TopArtists
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.json.JSONException
import java.util.*

class TopArtistsAdapter : TypeAdapter<TopArtists>() {

    private val TOPARTISTS_KEY = "topartists"
    private val MAX_IMAGE_RESOLUTION_INDEX = 3

    private val mGson = Gson()

    @Throws(JSONException::class)
    override fun write(jsonWriter: JsonWriter, topArtists: TopArtists) {
        mGson.toJson(topArtists, TopArtists::class.java, jsonWriter)
    }

    @Throws(JSONException::class)
    override fun read(jsonReader: JsonReader): TopArtists {

        val artists = ArrayList<Artist>()

        val rootObject = mGson.getAdapter(JsonElement::class.java).read(jsonReader).asJsonObject

        if (rootObject.has("error")) throw APIException(rootObject.get("message").asString)

        val totalArtists = rootObject.get(TOPARTISTS_KEY).asJsonObject.get(ATTRIBUTE_KEY).asJsonObject.get(TOTAL_KEY).asInt
        val artistsJsonArray = rootObject.get(TOPARTISTS_KEY).asJsonObject.getAsJsonArray(ARTIST_KEY)

        for (i in 0 until artistsJsonArray.size()) {
            val artistJsonObject = artistsJsonArray.get(i).asJsonObject

            val name = artistJsonObject.get(NAME_KEY).asString

            val listenersCount = artistJsonObject.get(LISTENERS_KEY)?.asInt

            val playCount = artistJsonObject.get(PLAYCOUNT_KEY)?.asString

            val url = artistJsonObject.get(URL_KEY).asString

            val jsonArray = artistJsonObject.get(IMAGE_KEY).asJsonArray
            var imageUrl = jsonArray.get(MAX_IMAGE_RESOLUTION_INDEX).asJsonObject.get(TEXT_KEY).asString
            imageUrl = if (imageUrl == EMPTY_STRING) null else imageUrl

            val rank = artistJsonObject.get(ATTRIBUTE_KEY).asJsonObject.get(RANK_KEY)?.asString

            artists.add(Artist(name, listenersCount, playCount, url, imageUrl, rank))
        }

        return TopArtists(artists, totalArtists)
    }
}
