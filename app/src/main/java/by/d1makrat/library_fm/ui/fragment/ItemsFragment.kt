package by.d1makrat.library_fm.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import by.d1makrat.library_fm.R
import by.d1makrat.library_fm.adapter.list.ItemsAdapter
import by.d1makrat.library_fm.presenter.fragment.ItemsPresenter
import by.d1makrat.library_fm.view.fragment.ItemsView
import by.d1makrat.library_fm.ui.CenteredToast

abstract class ItemsFragment<T, V: ItemsView<T>, P: ItemsPresenter<T, V>>: Fragment(), ItemsView<T> {

    protected var presenter: P? = null

    protected var mListAdapter: ItemsAdapter<T>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        mListAdapter = createAdapter(layoutInflater)
    }

    override fun showError(message: String) {
        mListAdapter?.removeAllHeadersAndFooters()

        if (mListAdapter?.isEmpty!!) {
            mListAdapter?.addErrorHeader()
        }

        CenteredToast.show(activity, message, Toast.LENGTH_SHORT)
    }

    protected abstract fun createAdapter(layoutInflater: LayoutInflater): ItemsAdapter<T>

    protected fun setUpRecyclerView(pRootView: View) {

        val mRecyclerView: RecyclerView = pRootView.findViewById(R.id.rv)

        mRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.adapter = mListAdapter
        mRecyclerView.addOnScrollListener(recyclerViewOnScrollListener)
        registerForContextMenu(mRecyclerView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.detachView()
    }

    private val recyclerViewOnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val visibleItemCount = recyclerView.layoutManager.childCount
            val totalItemCount = recyclerView.layoutManager.itemCount
            val firstVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            if (firstVisibleItemPosition + visibleItemCount >= totalItemCount) {
                if (firstVisibleItemPosition == 0){
                    presenter?.loadFirstPage()
                }
                else presenter?.loadNextPage()
            }
        }
    }

    override fun showHeader() {
        mListAdapter?.addHeader()
    }

    override fun showFooter() {
        mListAdapter?.addFooter()
    }

    override fun clearList() {
        mListAdapter?.removeAll()
    }

    override fun populateList(items: List<T>) {
        mListAdapter?.addAll(items)
    }

    override fun removeAllHeadersAndFooters() {
        mListAdapter?.removeAllHeadersAndFooters()
    }

    override fun openBrowser(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        if (intent.resolveActivity(activity?.packageManager) != null) {
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }
}
