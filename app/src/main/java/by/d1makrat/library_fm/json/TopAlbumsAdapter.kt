package by.d1makrat.library_fm.json

import by.d1makrat.library_fm.APIException
import by.d1makrat.library_fm.Constants.*
import by.d1makrat.library_fm.Constants.JsonConstants.*
import by.d1makrat.library_fm.model.Album
import by.d1makrat.library_fm.model.TopAlbums
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.json.JSONException
import java.util.*

class TopAlbumsAdapter : TypeAdapter<TopAlbums>() {

    private val TOPALBUMS_KEY = "topalbums"
    private val MAX_IMAGE_RESOLUTION_INDEX = 3

    private val mGson = Gson()

    @Throws(JSONException::class)
    override fun write(jsonWriter: JsonWriter, topAlbums: TopAlbums) {
        mGson.toJson(topAlbums, TopAlbums::class.java, jsonWriter)
    }

    @Throws(JSONException::class)
    override fun read(jsonReader: JsonReader): TopAlbums {

        val albums = ArrayList<Album>()

        val rootObject = mGson.getAdapter(JsonElement::class.java).read(jsonReader).asJsonObject

        if (rootObject.has("error")) throw APIException(rootObject.get("message").asString)

        val totalAlbums = rootObject.get(TOPALBUMS_KEY).asJsonObject.get(ATTRIBUTE_KEY).asJsonObject.get(TOTAL_KEY).asString
        val albumsJsonArray = rootObject.get(TOPALBUMS_KEY).asJsonObject.getAsJsonArray(ALBUM_KEY)

        for (i in 0 until albumsJsonArray.size()) {
            val albumJsonObject = albumsJsonArray.get(i).asJsonObject

            val title = albumJsonObject.get(NAME_KEY).asString

            val artistName = albumJsonObject.get(ARTIST_KEY).asJsonObject.get(NAME_KEY).asString

            val playCount = albumJsonObject.get(PLAYCOUNT_KEY).asInt

            val jsonArray = albumJsonObject.get(IMAGE_KEY).asJsonArray
            var imageUrl = jsonArray.get(MAX_IMAGE_RESOLUTION_INDEX).asJsonObject.get(TEXT_KEY).asString
            imageUrl = if (imageUrl == EMPTY_STRING) null else imageUrl

            val rank = albumJsonObject.get(ATTRIBUTE_KEY).asJsonObject.get(RANK_KEY).asInt

            albums.add(Album(title, artistName, imageUrl, playCount, rank))
        }

        return TopAlbums(albums, totalAlbums)
    }
}
