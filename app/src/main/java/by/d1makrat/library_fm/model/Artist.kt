package by.d1makrat.library_fm.model

import android.arch.persistence.room.*

@Entity(tableName = "TopArtists", indices = [(Index(value = ["rank", "period"], unique = true))])
class Artist (

        @PrimaryKey
        val id: Long?,

        @ColumnInfo(name="artist")
        val name: String,

        val playCount: Int? = null,

        val url: String,

        val imageUri: String?,

        val rank: Int? = null,

        var period: String? = null
){
    var listenersCount: Int? = null

    @Ignore
    constructor(name: String, listenersCount: Int, url: String, imageUri: String?):
            this(null, name, null, url, imageUri, null){
        this.listenersCount = listenersCount
    }

    @Ignore
    constructor(name: String, playCount: Int, url: String, imageUri: String?, rank: Int):
            this(null, name, playCount, url, imageUri, rank)
}
