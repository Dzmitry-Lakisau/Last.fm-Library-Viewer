package by.d1makrat.library_fm.model

class Artist(val name: String, val listenersCount: Int?, val playCount: String?, val url: String?, val imageUrl: String?, val rank: String?){

    constructor(name: String, playCount: String, imageUrl: String, rank: String): this(name, null, playCount, null, imageUrl, rank)

    constructor(name: String, listenersCount: Int, url: String, imageUrl: String): this(name, listenersCount, null, url, imageUrl, null)
}
