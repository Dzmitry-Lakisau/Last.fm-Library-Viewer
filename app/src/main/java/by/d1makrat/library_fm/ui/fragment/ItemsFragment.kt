package by.d1makrat.library_fm.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import by.d1makrat.library_fm.R
import by.d1makrat.library_fm.adapter.list.ItemsAdapter
import by.d1makrat.library_fm.presenter.fragment.ItemsPresenter
import by.d1makrat.library_fm.view.ItemsView
import by.d1makrat.library_fm.ui.CenteredToast

abstract class ItemsFragment<T, V: ItemsView<T>, P: ItemsPresenter<T, V>>: Fragment(), ItemsView<T> {

    protected var presenter: P? = null

    protected var mListAdapter: ItemsAdapter<T>? = null

    private var mLayoutManager: LinearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        mListAdapter = createAdapter(layoutInflater)
//        presenter = createPresenter()
    }

    override fun showError(message: String?) {
        mListAdapter?.removeAllHeadersAndFooters()

        if (mListAdapter?.isEmpty!!) {
            mListAdapter?.addErrorHeader()
        }

        CenteredToast.show(activity, message, Toast.LENGTH_SHORT)
    }

    protected abstract fun createAdapter(layoutInflater: LayoutInflater): ItemsAdapter<T>

//    protected abstract fun createPresenter(): ItemsPresenter<T>

    protected abstract fun setUpActionBar(activity: AppCompatActivity?)

    protected fun setUpRecyclerView(pRootView: View) {

        val mRecyclerView: RecyclerView = pRootView.findViewById(R.id.rv)

//        mLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.adapter = mListAdapter
        mRecyclerView.addOnScrollListener(recyclerViewOnScrollListener)
        registerForContextMenu(mRecyclerView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.detachView()
    }

    private val recyclerViewOnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val visibleItemCount = mLayoutManager.childCount
            val totalItemCount = mLayoutManager.itemCount
            val firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition()
            if (firstVisibleItemPosition + visibleItemCount >= totalItemCount && totalItemCount > 0) {
                presenter?.loadItems()
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

    override fun populateList(items: List<T>?) {
        mListAdapter?.addAll(items)
    }

    override fun removeAllHeadersAndFooters() {
        mListAdapter?.removeAllHeadersAndFooters()
    }

    override fun openBrowser(uri: Uri) {
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }
}
