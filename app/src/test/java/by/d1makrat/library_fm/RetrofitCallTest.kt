package by.d1makrat.library_fm

import by.d1makrat.library_fm.https.LastFmRestApiService
import junit.framework.Assert.assertTrue
import org.junit.Test
import retrofit2.Retrofit
import java.io.IOException

class RetrofitCallTest {

    @Test
    fun searchArtist() {

        var count: Int? = null

        val retrofit = Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .build()

        val mLastFmRestApiService = retrofit.create(LastFmRestApiService::class.java)

        val call = mLastFmRestApiService.searchArtist("The", 1, 10)

        try {
            val response = call.execute()

            if (response.isSuccessful){
                val json: String? = response.body()?.string()

                count = json?.split("name")?.size?.minus(1)
            }

            assertTrue(count == 10)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
