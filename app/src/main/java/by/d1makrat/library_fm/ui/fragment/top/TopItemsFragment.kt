package by.d1makrat.library_fm.ui.fragment.top

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import by.d1makrat.library_fm.Constants
import by.d1makrat.library_fm.Constants.PERIOD_KEY
import by.d1makrat.library_fm.R
import by.d1makrat.library_fm.presenter.fragment.top.TopItemsPresenter
import by.d1makrat.library_fm.ui.CenteredToast
import by.d1makrat.library_fm.ui.fragment.ItemsFragment
import by.d1makrat.library_fm.view.fragment.TopItemsView
import java.text.DecimalFormat

abstract class TopItemsFragment<T>: ItemsFragment<T, TopItemsView<T>, TopItemsPresenter<T>>(), TopItemsView<T> {

    protected val MENU_SCROBBLES_OF_ARTIST = 0
    protected val MENU_SCROBBLES_OF_TRACK = 1
    protected val MENU_SCROBBLES_OF_ALBUM = 2

    protected var mPeriod: String? = null
    protected var listHeadTextView: TextView? = null

    protected lateinit var listHeadMessage: String
    protected lateinit var allIsLoadedMessage: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPeriod = arguments?.getString(PERIOD_KEY)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.list_with_head, container, false)

        presenter?.attachView(this)

        setUpRecyclerView(rootView)

        listHeadTextView = rootView.findViewById(R.id.list_head)

        if (mListAdapter!!.isEmpty) {
            presenter?.onCreatingNewView()

        } else {
            presenter?.onShowingFromBackStack()
        }

        return rootView
    }

    override fun showEmptyHeader() {
        mListAdapter?.addEmptyHeader(getString(R.string.empty_rankings))
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
            R.id.open_in_browser -> {
                presenter?.onOpenInBrowser()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun hideListHead() {
        listHeadTextView?.visibility = View.INVISIBLE
    }

    override fun showListHead(itemCount: Int) {
        val formattedItemCount = DecimalFormat(Constants.NUMBER_FORMATTING_PATTERN).format(itemCount)
        listHeadTextView?.text = listHeadMessage.format(formattedItemCount)
        listHeadTextView?.visibility = View.VISIBLE
    }

    override fun showAllIsLoaded() {
        CenteredToast.show(context, allIsLoadedMessage, Toast.LENGTH_SHORT)
    }
}
