package by.d1makrat.library_fm.model

open class Artist(val name: String,
                  val listenersCount: Int? = null,
                  val playCount: Int? = null,
                  val url: String? = null,
                  val imageUrl: String? = null,
                  val rank: String? = null,
                  val stats: Stats? = null) {

    constructor(name: String, imageUrl: String?, playCount: Int, rank: String) : this(name, null, playCount, null, imageUrl, rank)

    constructor(name: String, listenersCount: Int, url: String, imageUrl: String?) : this(name, listenersCount, null, url, imageUrl, null)
}
