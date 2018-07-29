package by.d1makrat.library_fm.json.model

import by.d1makrat.library_fm.model.Artist

class ArtistsJsonModel {

    private var artists = ArrayList<Artist>()

    fun add(artist: Artist){
        artists.add(artist)
    }

    fun get(index: Int): Artist {
        return artists[index]
    }

    fun getAll(): List<Artist> {
        return artists
    }
}
