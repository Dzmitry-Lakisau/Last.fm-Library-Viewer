package by.d1makrat.library_fm.json.model

import by.d1makrat.library_fm.model.Scrobble

class ScrobblesJsonModel {

    private var scrobbles = ArrayList<Scrobble>()

    fun add(scrobble: Scrobble){
        scrobbles.add(scrobble)
    }

    fun get(index: Int): Scrobble {
        return scrobbles[index]
    }

    fun getAll(): List<Scrobble> {
        return scrobbles
    }
}
