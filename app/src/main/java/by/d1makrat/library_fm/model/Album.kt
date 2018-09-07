package by.d1makrat.library_fm.model

import android.arch.persistence.room.*

@Entity(tableName = "TopAlbums", indices = [(Index(value = ["rank", "period"], unique = true))])
data class Album (

        @PrimaryKey
        val id: Long?,

        @ColumnInfo(name="album")
        val title: String,

        @ColumnInfo(name="artist")
        val artistName: String,

        val playCount: Int,

        val imageUri: String?,

        val rank: Int,

        var period: String? = null
){
    @Ignore
    constructor(title: String, artistName: String, imageUri: String?, playCount: Int, rank: Int):
            this(null, title, artistName, playCount, imageUri, rank)
}
