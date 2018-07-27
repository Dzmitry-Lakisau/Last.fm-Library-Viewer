package by.d1makrat.library_fm.https

import android.support.annotation.NonNull
import android.support.annotation.Nullable
import by.d1makrat.library_fm.BuildConfig.API_KEY
import by.d1makrat.library_fm.json.model.ArtistsJsonModel
import by.d1makrat.library_fm.json.model.ScrobblesJsonModel
import by.d1makrat.library_fm.model.TopAlbums
import by.d1makrat.library_fm.model.TopArtists
import by.d1makrat.library_fm.model.TopTracks
import by.d1makrat.library_fm.model.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LastFmRestApiService {

    @POST("?api_key=$API_KEY&format=json&method=auth.getMobileSession")
    fun getSessionKey(@NonNull
                      @Query("api_sig") signature: String,
                      @NonNull
                      @Query("username") username: String,
                      @NonNull
                      @Query("password") password: String): Call<ResponseBody>

    @POST("?api_key=$API_KEY&format=json&method=track.scrobble")
    fun sendScrobble(@NonNull
                     @Query("api_sig") signature: String,
                     @NonNull
                     @Query("sk") sessionKey: String,
                     @NonNull
                     @Query("track") track: String,
                     @NonNull
                     @Query("artist") artist: String,
                     @Nullable
                     @Query("album") album: String?,
                     @Nullable
                     @Query("trackNumber") trackNumber: Int?,
                     @Nullable
                     @Query("duration") trackDuration: Int?,
                     @NonNull
                     @Query("timestamp") timestamp: Int): Call<ResponseBody>

    @GET("?api_key=$API_KEY&format=json&method=user.getRecentTracks")
    fun getRecentScrobbles(@NonNull
                           @Query("user") username: String,
                           @NonNull
                           @Query("page") page: Int,
                           @NonNull
                           @Query("limit") scrobblesPerRequest: Int,
                           @Nullable
                           @Query("from") startOfPeriod: Long?,
                           @Nullable
                           @Query("to") endOfPeriod: Long?): Call<ScrobblesJsonModel>

    @GET("?api_key=$API_KEY&format=json&method=user.getArtistTracks")
    fun getScrobblesOfArtist(@NonNull
                             @Query("user") username: String,
                             @NonNull
                             @Query("artist") artist: String,
                             @NonNull
                             @Query("page") page: Int,
                             @NonNull
                             @Query("limit") scrobblesPerRequest: Int,
                             @Nullable
                             @Query("startTimestamp") startOfPeriod: Long?,
                             @Nullable
                             @Query("endTimestamp") endOfPeriod: Long?): Call<ScrobblesJsonModel>

    @GET("?api_key=$API_KEY&format=json&method=user.getTopAlbums")
    fun getTopAlbums(@NonNull
                     @Query("user") username: String,
                     @NonNull
                     @Query("period") period: String,
                     @NonNull
                     @Query("page") page: Int,
                     @NonNull
                     @Query("limit") scrobblesPerRequest: Int): Call<TopAlbums>

    @GET("?api_key=$API_KEY&format=json&method=user.getTopArtists")
    fun getTopArtists(@NonNull
                     @Query("user") username: String,
                     @NonNull
                     @Query("period") period: String,
                     @NonNull
                     @Query("page") page: Int,
                     @NonNull
                     @Query("limit") scrobblesPerRequest: Int): Call<TopArtists>

    @GET("?api_key=$API_KEY&format=json&method=user.getTopTracks")
    fun getTopTracks(@NonNull
                     @Query("user") username: String,
                     @NonNull
                     @Query("period") period: String,
                     @NonNull
                     @Query("page") page: Int,
                     @NonNull
                     @Query("limit") scrobblesPerRequest: Int): Call<TopTracks>


    @GET("?api_key=$API_KEY&format=json&method=user.getInfo")
    fun getUserInfo(@NonNull
                    @Query("user") signature: String): Call<User>

    @GET("?api_key=$API_KEY&format=json&method=artist.search")
    fun searchArtist(@NonNull
                     @Query("artist") name: String,
                     @NonNull
                     @Query("page") page: Int,
                     @NonNull
                     @Query("limit") artistsPerRequest: Int): Call<ArtistsJsonModel>
}
