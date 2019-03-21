package by.d1makrat.library_fm.ui.fragment.top

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.R
import by.d1makrat.library_fm.adapter.list.ItemsAdapter
import by.d1makrat.library_fm.adapter.list.TopArtistsAdapter
import by.d1makrat.library_fm.model.Artist
import by.d1makrat.library_fm.presenter.fragment.top.TopArtistsPresenter
import by.d1makrat.library_fm.ui.activity.MainActivity

class TopArtistsFragment: TopItemsFragment<Artist>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = TopArtistsPresenter(mPeriod!!)

        listHeadMessage = getString(R.string.total_artists)
        allIsLoadedMessage = getString(R.string.all_artists_are_loaded)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        menu.add(mPeriod!!.hashCode(), MENU_SCROBBLES_OF_ARTIST, 0, R.string.scrobbles_of_artist)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        return if (item?.groupId == mPeriod!!.hashCode()) {
            val listItemPressed = mListAdapter!!.selectedItem
            when (item.itemId) {
                MENU_SCROBBLES_OF_ARTIST -> {
                    (activity as MainActivity).openScrobblesOfArtistFragment(listItemPressed.name)
                    true
                }
                else -> super.onContextItemSelected(item)
            }
        }
        else {
            super.onContextItemSelected(item)
        }
    }

    override fun createAdapter(layoutInflater: LayoutInflater): ItemsAdapter<Artist> {
        return TopArtistsAdapter(layoutInflater, ContextCompat.getDrawable(AppContext.getInstance(), R.drawable.ic_person_black_24dp_large))
    }
}
