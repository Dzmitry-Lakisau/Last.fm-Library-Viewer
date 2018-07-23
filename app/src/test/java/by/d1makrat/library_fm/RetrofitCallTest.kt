package by.d1makrat.library_fm

import by.d1makrat.library_fm.Constants.DATE_PERIODS_FOR_API
import by.d1makrat.library_fm.https.LastFmRestApiService
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import java.io.IOException

class RetrofitCallTest {

    private val countOfObjectInResponse = 10
    private val user = "D1MAkrat"

    private lateinit var mLastFmRestApiService: LastFmRestApiService

    @Before
    fun setUp(){
        val retrofit = Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .build()

        mLastFmRestApiService = retrofit.create(LastFmRestApiService::class.java)

    }

    @Test
    fun getRecentScrobbles(){
        var count: Int? = null
        val startOfPeriod: Long? = 1000000
        val endOfPeriod: Long? = 1532342959

        val call = mLastFmRestApiService.getRecentScrobbles(user, 1, countOfObjectInResponse, startOfPeriod, endOfPeriod)

        try {
            val response = call.execute()

            if (response.isSuccessful){
                val json: String? = response.body()?.string()

                count = json?.split("name")?.size?.minus(1)
            }

            assertTrue(count == countOfObjectInResponse)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Test
    fun getScrobblesOfArtist(){
        var count: Int? = null
        val artist = "Queen"
        val startOfPeriod: Long? = 1000000
        val endOfPeriod: Long? = 1532342959

        val call = mLastFmRestApiService.getScrobblesOfArtist(user,  artist, 1, countOfObjectInResponse, startOfPeriod, endOfPeriod)

        try {
            val response = call.execute()

            if (response.isSuccessful){
                val json: String? = response.body()?.string()

                count = json?.split("name")?.size?.minus(1)
            }

            assertTrue(count == countOfObjectInResponse)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Test
    fun getTopAlbums(){
        var count: Int? = null

        val call = mLastFmRestApiService.getTopAlbums(user, DATE_PERIODS_FOR_API[4], 1, countOfObjectInResponse)

        try {
            val response = call.execute()

            if (response.isSuccessful){
                val json: String? = response.body()?.string()

                count = json?.split("playcount")?.size?.minus(1)
            }

            assertTrue(count == countOfObjectInResponse)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Test
    fun getTopArtists(){
        var count: Int? = null

        val call = mLastFmRestApiService.getTopArtists(user, DATE_PERIODS_FOR_API[4], 1, countOfObjectInResponse)

        try {
            val response = call.execute()

            if (response.isSuccessful){
                val json: String? = response.body()?.string()

                count = json?.split("playcount")?.size?.minus(1)
            }

            assertTrue(count == countOfObjectInResponse)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Test
    fun getTopTracks(){
        var count: Int? = null

        val call = mLastFmRestApiService.getTopTracks(user, DATE_PERIODS_FOR_API[4], 1, countOfObjectInResponse)

        try {
            val response = call.execute()

            if (response.isSuccessful){
                val json: String? = response.body()?.string()

                count = json?.split("playcount")?.size?.minus(1)
            }

            assertTrue(count == countOfObjectInResponse)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Test
    fun getUserInfo(){
        lateinit var json: String

        val call = mLastFmRestApiService.getUserInfo(user)

        try {
            val response = call.execute()

            if (response.isSuccessful){
                json = response.body()!!.string()
            }

            assertTrue(json.contains(user))

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Test
    fun searchArtist() {
        var count: Int? = null
        val query = "The"

        val call = mLastFmRestApiService.searchArtist(query, 1, countOfObjectInResponse)

        try {
            val response = call.execute()

            if (response.isSuccessful){
                val json: String? = response.body()?.string()

                count = json?.split("name")?.size?.minus(1)
            }

            assertTrue(count == countOfObjectInResponse)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
