package by.d1makrat.library_fm.retrofit

import android.support.annotation.NonNull
import android.support.annotation.Nullable
import by.d1makrat.library_fm.BuildConfig.API_KEY
import by.d1makrat.library_fm.json.model.ArtistsJsonModel
import by.d1makrat.library_fm.json.model.ScrobblesJsonModel
import by.d1makrat.library_fm.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LastFmRestApiService {

    @POST("?api_key=$API_KEY&method=auth.getMobileSession")
    fun getSessionKey(@NonNull
                      @Query("username") username: String,
                      @NonNull
                      @Query("password") password: String): Call<SessionKey>

    @POST("?api_key=$API_KEY&method=track.scrobble")
    fun sendScrobble(@NonNull
                     @Query("sk") sessionKey: String,
                     @NonNull
                     @Query("track") track: String,
                     @NonNull
                     @Query("artist") artist: String,
                     @Nullable
                     @Query("album") album: String?,
                     @Nullable
                     @Query("trackNumber") trackNumber: String?,
                     @Nullable
                     @Query("duration") trackDuration: String,
                     @NonNull
                     @Query("timestamp") timestamp: Long): Call<SendScrobbleResult>

    @GET("?api_key=$API_KEY&method=user.getRecentTracks")
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

    @GET("?api_key=$API_KEY&method=user.getArtistTracks")
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

    @GET("?api_key=$API_KEY&method=user.getTopAlbums")
    fun getTopAlbums(@NonNull
                     @Query("user") username: String,
                     @NonNull
                     @Query("period") period: String,
                     @NonNull
                     @Query("page") page: Int,
                     @NonNull
                     @Query("limit") scrobblesPerRequest: Int): Call<TopAlbums>

    @GET("?api_key=$API_KEY&method=user.getTopArtists")
    fun getTopArtists(@NonNull
                     @Query("user") username: String,
                     @NonNull
                     @Query("period") period: String,
                     @NonNull
                     @Query("page") page: Int,
                     @NonNull
                     @Query("limit") scrobblesPerRequest: Int): Call<TopArtists>

    @GET("?api_key=$API_KEY&method=user.getTopTracks")
    fun getTopTracks(@NonNull
                     @Query("user") username: String,
                     @NonNull
                     @Query("period") period: String,
                     @NonNull
                     @Query("page") page: Int,
                     @NonNull
                     @Query("limit") scrobblesPerRequest: Int): Call<TopTracks>


    @GET("?api_key=$API_KEY&method=user.getInfo")
    fun getUserInfo(@Query("user") username: String?): Call<User>

    @GET("?api_key=$API_KEY&method=artist.search")
    fun searchArtist(@NonNull
                     @Query("artist") name: String,
                     @NonNull
                     @Query("page") page: Int,
                     @NonNull
                     @Query("limit") artistsPerRequest: Int): Call<ArtistsJsonModel>
}
