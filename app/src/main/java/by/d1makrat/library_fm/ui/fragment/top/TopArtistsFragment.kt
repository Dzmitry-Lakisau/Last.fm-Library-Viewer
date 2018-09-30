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
import by.d1makrat.library_fm.adapter.list.TopArtistsAdapter
import by.d1makrat.library_fm.model.Artist
import by.d1makrat.library_fm.presenter.fragment.top.TopArtistsPresenter
import by.d1makrat.library_fm.ui.CenteredToast
import by.d1makrat.library_fm.ui.activity.MainActivity

class TopArtistsFragment: TopItemsFragment<Artist>() {

    @SuppressLint("MissingSuperCall")//TODO remove
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = TopArtistsPresenter(mPeriod!!)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        menu.add(mPeriod!!.hashCode(), MENU_SCROBBLES_OF_ARTIST, 0, R.string.scrobbles_of_artist)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        return if (item?.groupId == mPeriod!!.hashCode()) {
            when (item.itemId) {
                MENU_SCROBBLES_OF_ARTIST -> {
                    (activity as MainActivity).showScrobblesOfArtistFragment(mListAdapter!!.selectedItem.name)
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
        listHeadTextView?.text = getString(R.string.total_artists, itemCount)
        listHeadTextView?.visibility = View.VISIBLE
    }

    override fun createAdapter(layoutInflater: LayoutInflater): ItemsAdapter<Artist> {
        return TopArtistsAdapter(layoutInflater, ContextCompat.getDrawable(AppContext.getInstance(), R.drawable.img_vinyl))
    }

    override fun showAllIsLoaded() {
        CenteredToast.show(context, R.string.all_artists_are_loaded, Toast.LENGTH_SHORT)
    }
}
