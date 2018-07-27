package by.d1makrat.library_fm

import by.d1makrat.library_fm.Constants.API_BASE_URL
import by.d1makrat.library_fm.Constants.DATE_PERIODS_FOR_API
import by.d1makrat.library_fm.https.LastFmRestApiService
import by.d1makrat.library_fm.json.*
import by.d1makrat.library_fm.json.model.ArtistsJsonModel
import by.d1makrat.library_fm.json.model.ScrobblesJsonModel
import by.d1makrat.library_fm.model.*
import com.google.gson.GsonBuilder
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RetrofitCallTest {

    private val countOfObjectInResponse = 10
    private val user: String = "D1MAkrat"

    private lateinit var mLastFmRestApiService: LastFmRestApiService


    @Before
    fun setUp(){
        val retrofit = Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                        .registerTypeAdapter(User::class.java, UserAdapter())
                        .registerTypeAdapter(TopAlbums::class.java, TopAlbumsAdapter())
                        .registerTypeAdapter(TopArtists::class.java, TopArtistsAdapter())
                        .registerTypeAdapter(TopTracks::class.java, TopTracksAdapter())
                        .registerTypeAdapter(ArtistsJsonModel::class.java, SearchArtistResultsAdapter())
                        .registerTypeAdapter(ScrobblesJsonModel::class.java, ScrobblesAdapter())
                        .create()))
                .addConverterFactory(GsonConverterFactory.create(
                        GsonBuilder().registerTypeAdapter(User::class.java, UserAdapter()).create()))
                .build()

        mLastFmRestApiService = retrofit.create(LastFmRestApiService::class.java)

    }

    @Test
    fun getRecentScrobbles(){
        var scrobbles: List<Scrobble>? = null
        val startOfPeriod: Long? = 1000000
        val endOfPeriod: Long? = 1532342959

        val call = mLastFmRestApiService.getRecentScrobbles(user, 1, countOfObjectInResponse, startOfPeriod, endOfPeriod)

        try {
            val response = call.execute()

            if (response.isSuccessful){
                scrobbles = response.body()?.getAll()
            }

            assert(scrobbles?.size == countOfObjectInResponse)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Test
    fun getScrobblesOfArtist(){
        var scrobbles: List<Scrobble>? = null
        val artist = "Queen"
        val startOfPeriod: Long? = 1000000
        val endOfPeriod: Long? = 1532342959

        val call = mLastFmRestApiService.getScrobblesOfArtist(user,  artist, 1, countOfObjectInResponse, startOfPeriod, endOfPeriod)

        try {
            val response = call.execute()

            if (response.isSuccessful){
                scrobbles = response.body()?.getAll()
            }

            assertTrue(scrobbles?.size == countOfObjectInResponse)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Test
    fun getTopAlbums(){
        var result: TopAlbums? = null

        val call = mLastFmRestApiService.getTopAlbums(user, DATE_PERIODS_FOR_API[4], 1, countOfObjectInResponse)

        try {
            val response = call.execute()

            if (response.isSuccessful){
                result = response.body()
            }

            assert(result?.total == countOfObjectInResponse.toString())

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Test
    fun getTopArtists(){
        var result: TopArtists? = null

        val call = mLastFmRestApiService.getTopArtists(user, DATE_PERIODS_FOR_API[4], 1, countOfObjectInResponse)

        try {
            val response = call.execute()

            if (response.isSuccessful){
                result = response.body()
            }

            assert(result?.total == countOfObjectInResponse.toString())

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Test
    fun getTopTracks(){
        var result: TopTracks? = null

        val call = mLastFmRestApiService.getTopTracks(user, DATE_PERIODS_FOR_API[4], 1, countOfObjectInResponse)

        try {
            val response = call.execute()

            if (response.isSuccessful){
                result = response.body()
            }

            assert(result?.total == countOfObjectInResponse.toString())

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Test
    fun getUserInfo(){
        var json: User? = null

        val call = mLastFmRestApiService.getUserInfo(user)

        try {
            val response = call.execute()

            if (response.isSuccessful){
                json = response.body()
            }

            assert(json?.username == user)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Test
    fun searchArtist() {
        var artists: List<Artist>? = null
        val query = "The"

        val call = mLastFmRestApiService.searchArtist(query, 1, countOfObjectInResponse)

        try {
            val response = call.execute()

            if (response.isSuccessful){
                artists = response.body()?.getAll()
            }

            assertTrue(artists?.size == countOfObjectInResponse)

        } catch (e: Exception) {
            e.printStackTrace()
            fail()
        }
    }
}
