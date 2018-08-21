package by.d1makrat.library_fm.ui.fragment.top

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.R
import by.d1makrat.library_fm.adapter.list.ItemsAdapter
import by.d1makrat.library_fm.adapter.list.TopAlbumsAdapter
import by.d1makrat.library_fm.asynctask.GetTopItemsAsyncTask
import by.d1makrat.library_fm.model.TopAlbum
import by.d1makrat.library_fm.operation.TopAlbumsOperation
import by.d1makrat.library_fm.ui.CenteredToast

class TopAlbumsFragment: TopItemsFragment<TopAlbum>() {

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        menu?.add(mPeriod.hashCode(), TopItemsFragment.MENU_SCROBBLES_OF_ARTIST, 0, R.string.scrobbles_of_artist)
        menu?.add(mPeriod.hashCode(), TopItemsFragment.MENU_SCROBBLES_OF_ALBUM, 1, R.string.scrobbles_of_album)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        return if (item?.groupId == mPeriod.hashCode()) {
            when (item.itemId) {
                TopItemsFragment.MENU_SCROBBLES_OF_ARTIST -> {
                    replaceFragment(mListAdapter.selectedItem.artistName, null, null)
                    true
                }
                TopItemsFragment.MENU_SCROBBLES_OF_ALBUM -> {
                    val listItemPressed = mListAdapter.selectedItem
                    replaceFragment(listItemPressed.artistName, null, listItemPressed.title)
                    true
                }
                else -> super.onContextItemSelected(item)
            }
        }
        else {
            super.onContextItemSelected(item)
        }
    }

    override fun setUpListHead(pItemsCount: String?, pVisibility: Int) {
        listHeadTextView?.visibility = pVisibility
        if (pVisibility == View.VISIBLE) {
            listHeadTextView?.text = getString(R.string.total_albums, pItemsCount)
        }
    }

    override fun checkIfAllIsLoaded(size: Int) {
        if (size < AppContext.getInstance().limit) {
            mListAdapter.allIsLoaded = true
            CenteredToast.show(context, R.string.all_albums_are_loaded, Toast.LENGTH_SHORT)
        }
    }

    override fun createAdapter(pLayoutInflater: LayoutInflater): ItemsAdapter<TopAlbum> {
        return TopAlbumsAdapter(pLayoutInflater, ContextCompat.getDrawable(AppContext.getInstance(), R.drawable.img_vinyl))
    }

    override fun performOperation() {
        val topAlbumsOperation = TopAlbumsOperation(mPeriod, mPage)
        val getTopItemsAsyncTask = GetTopItemsAsyncTask(this)
        getTopItemsAsyncTask.execute(topAlbumsOperation)
    }

    override fun setUpActionBar(pActivity: AppCompatActivity) {
        pActivity.supportActionBar?.setTitle(R.string.top_albums)
    }
}
