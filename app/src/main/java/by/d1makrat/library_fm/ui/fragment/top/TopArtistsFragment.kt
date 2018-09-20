package by.d1makrat.library_fm.ui.fragment.top

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.R
import by.d1makrat.library_fm.adapter.list.ItemsAdapter
import by.d1makrat.library_fm.adapter.list.TopArtistsAdapter
import by.d1makrat.library_fm.model.Album
import by.d1makrat.library_fm.model.Artist
import by.d1makrat.library_fm.presenter.fragment.top.TopArtistsPresenter
import by.d1makrat.library_fm.ui.CenteredToast

class TopArtistsFragment: TopItemsFragment<Artist>() {

    @SuppressLint("MissingSuperCall")//TODO remove
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = TopArtistsPresenter(mPeriod!!)
        presenter?.loadItems()
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo)

        menu.add(mPeriod!!.hashCode(), MENU_SCROBBLES_OF_ARTIST, 0, R.string.scrobbles_of_artist)
        menu.add(mPeriod!!.hashCode(), MENU_SCROBBLES_OF_ALBUM, 1, R.string.scrobbles_of_album)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        return super.onContextItemSelected(item)
        //TODO send to activity
    }

    override fun hideListHead() {
        listHeadTextView?.visibility = View.INVISIBLE
    }

    override fun showListHead(itemCount: String) {
        listHeadTextView?.text = getString(R.string.total_artists, itemCount)
        listHeadTextView?.visibility = View.VISIBLE
    }

    override fun createAdapter(layoutInflater: LayoutInflater): ItemsAdapter<Artist> {
        return TopArtistsAdapter(layoutInflater, ContextCompat.getDrawable(AppContext.getInstance(), R.drawable.img_vinyl))
    }

    override fun showAllIsLoaded() {
        CenteredToast.show(context, R.string.all_artists_are_loaded, Toast.LENGTH_SHORT)
    }

    override fun setUpActionBar(activity: AppCompatActivity?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
