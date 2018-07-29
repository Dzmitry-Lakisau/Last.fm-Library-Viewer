package by.d1makrat.library_fm.json

import by.d1makrat.library_fm.APIException
import by.d1makrat.library_fm.Constants.*
import by.d1makrat.library_fm.Constants.JsonConstants.*
import by.d1makrat.library_fm.json.model.ArtistsJsonModel
import by.d1makrat.library_fm.model.Artist
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.json.JSONException

class SearchArtistResultsAdapter : TypeAdapter<ArtistsJsonModel>() {

    private val MAX_IMAGE_RESOLUTION_INDEX = 4

    private val mGson = Gson()

    @Throws(JSONException::class)

    override fun write(jsonWriter: JsonWriter, artistsJsonModel: ArtistsJsonModel) {
        mGson.toJson(artistsJsonModel, ArtistsJsonModel::class.java, jsonWriter)
    }

    @Throws(JSONException::class)
    override fun read(jsonReader: JsonReader): ArtistsJsonModel {
        val artists = ArtistsJsonModel()

        val rootObject = mGson.getAdapter(JsonElement::class.java).read(jsonReader).asJsonObject

        if (rootObject.has("error")) throw APIException(rootObject.get("message").asString)

        val artistsJsonArray = rootObject.get("results").asJsonObject.get("artistmatches").asJsonObject.get(ARTIST_KEY).asJsonArray

        for (i in 0 until artistsJsonArray.size()) {
            val artistJsonObject = artistsJsonArray.get(i).asJsonObject

            val name = artistJsonObject.get(NAME_KEY).asString

            val listeners = artistJsonObject.get(LISTENERS_KEY).asString

            val url = artistJsonObject.get(URL_KEY).asString

            val jsonArray = artistJsonObject.get(IMAGE_KEY).asJsonArray
            var imageUrl = jsonArray.get(MAX_IMAGE_RESOLUTION_INDEX).asJsonObject.get(TEXT_KEY).asString
            imageUrl = if (imageUrl == EMPTY_STRING) null else imageUrl

            artists.add(Artist(name, listeners, null, url, imageUrl, null))
        }

        return artists
    }
}
