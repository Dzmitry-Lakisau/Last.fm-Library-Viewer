package by.d1makrat.library_fm.ui.fragment.top

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import by.d1makrat.library_fm.Constants.PERIOD_KEY
import by.d1makrat.library_fm.R
import by.d1makrat.library_fm.presenter.fragment.top.TopItemsPresenter
import by.d1makrat.library_fm.view.fragment.TopItemsView
import by.d1makrat.library_fm.ui.fragment.ItemsFragment

abstract class TopItemsFragment<T>: ItemsFragment<T, TopItemsView<T>, TopItemsPresenter<T>>(), TopItemsView<T> {

    protected val MENU_SCROBBLES_OF_ARTIST = 0
    protected val MENU_SCROBBLES_OF_TRACK = 1
    protected val MENU_SCROBBLES_OF_ALBUM = 2

    protected var mPeriod: String? = null
    protected var listHeadTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPeriod = arguments?.getString(PERIOD_KEY)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.list_with_head, container, false)

        setUpRecyclerView(rootView)

        listHeadTextView = rootView.findViewById(R.id.list_head)

        if (mListAdapter!!.isEmpty) {
            hideListHead()
        } else {
            showListHead(mListAdapter?.itemCount!!)
        }

        return rootView
    }

    override fun showEmptyHeader() {
        mListAdapter?.addEmptyHeader(getString(R.string.no_items))
    }

    override fun getListItemsCount(): Int {
        return mListAdapter?.itemCount ?: 0
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_refresh -> {
                presenter?.onRefresh()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
