package by.d1makrat.library_fm.model

import android.arch.persistence.room.*

@Entity(tableName = "TopTracks", indices = [(Index(value = ["rank", "period"], unique = true))])
data class Track (

        @PrimaryKey
        val id: Long?,

        @ColumnInfo(name="track")
        val title: String,

        @ColumnInfo(name="artist")
        val artistName: String,

        val playCount: Int,

        val imageUri: String?,

        val rank: Int,

        var period: String? = null
){
    @Ignore
    constructor(title: String, artistName: String, playCount: Int, imageUri: String?, rank: Int):
            this(null, title, artistName, playCount, imageUri, rank)
}
