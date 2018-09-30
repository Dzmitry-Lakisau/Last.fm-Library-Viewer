package by.d1makrat.library_fm.ui.fragment.top

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.R
import by.d1makrat.library_fm.adapter.list.ItemsAdapter
import by.d1makrat.library_fm.adapter.list.TopAlbumsAdapter
import by.d1makrat.library_fm.model.Album
import by.d1makrat.library_fm.presenter.fragment.top.TopAlbumsPresenter
import by.d1makrat.library_fm.ui.CenteredToast
import by.d1makrat.library_fm.ui.activity.MainActivity

class TopAlbumsFragment: TopItemsFragment<Album>() {

    @SuppressLint("MissingSuperCall")//TODO remove
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = TopAlbumsPresenter(mPeriod!!)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        menu.add(mPeriod!!.hashCode(), MENU_SCROBBLES_OF_ARTIST, 0, R.string.scrobbles_of_artist)
        menu.add(mPeriod!!.hashCode(), MENU_SCROBBLES_OF_ALBUM, 1, R.string.scrobbles_of_album)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        return if (item?.groupId == mPeriod!!.hashCode()) {
            when (item.itemId) {
                MENU_SCROBBLES_OF_ARTIST -> {
                    (activity as MainActivity).showScrobblesOfArtistFragment(mListAdapter!!.selectedItem.artistName)
                    true
                }
                MENU_SCROBBLES_OF_ALBUM -> {
                    val listItemPressed = mListAdapter!!.selectedItem
                    (activity as MainActivity).showScrobblesOfAlbumFragment(listItemPressed.artistName, listItemPressed.title)
                    true
                }
                else -> super.onContextItemSelected(item)
            }
        }
        else {
            super.onContextItemSelected(item)
        }
    }

    override fun hideListHead() {
        listHeadTextView?.visibility = View.INVISIBLE
    }

    override fun showListHead(itemCount: Int) {
        listHeadTextView?.text = getString(R.string.total_albums, itemCount)
        listHeadTextView?.visibility = View.VISIBLE
    }

    override fun createAdapter(layoutInflater: LayoutInflater): ItemsAdapter<Album> {
        return TopAlbumsAdapter(layoutInflater, ContextCompat.getDrawable(AppContext.getInstance(), R.drawable.img_vinyl))
    }

    override fun showAllIsLoaded() {
        CenteredToast.show(context, R.string.all_albums_are_loaded, Toast.LENGTH_SHORT)
    }
}
