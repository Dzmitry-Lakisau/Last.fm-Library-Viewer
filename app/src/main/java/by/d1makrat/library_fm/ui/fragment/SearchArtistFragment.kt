package by.d1makrat.library_fm.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import by.d1makrat.library_fm.Constants.ARTIST_KEY
import by.d1makrat.library_fm.R
import by.d1makrat.library_fm.adapter.list.ItemsAdapter
import by.d1makrat.library_fm.adapter.list.SearchArtistsAdapter
import by.d1makrat.library_fm.model.Artist
import by.d1makrat.library_fm.presenter.fragment.SearchArtistPresenter
import by.d1makrat.library_fm.ui.CenteredToast
import by.d1makrat.library_fm.ui.activity.MainActivity
import by.d1makrat.library_fm.utils.InputUtils
import by.d1makrat.library_fm.view.fragment.SearchArtistView
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*

class SearchArtistFragment: ItemsFragment<Artist, SearchArtistView<Artist>, SearchArtistPresenter>(), SearchArtistView<Artist> {

    private val MENU_SCROBBLES_OF_ARTIST = 0
    private val MENU_OPEN_IN_BROWSER = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = SearchArtistPresenter()

        val artist = savedInstanceState?.getString(ARTIST_KEY)
        if (artist != null){
            presenter!!.onSearchButtonPressed(artist)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_search, container, false)

        presenter?.attachView(this)

        setUpRecyclerView(rootView)

        rootView.search_button.setOnClickListener{presenter?.onSearchButtonPressed(search_field.text.toString())}

        rootView.search_field.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(editText: Editable) {
                rootView.search_button.isEnabled = editText.isNotEmpty()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        rootView.search_field.setOnKeyListener(View.OnKeyListener { _, keyCode: Int, event: KeyEvent ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                presenter?.onSearchButtonPressed(search_field.text.toString())
                return@OnKeyListener true
            }
            false
        })

        return rootView
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(ARTIST_KEY, presenter?.searchQuery)
    }

    override fun showEmptyHeader() {
        mListAdapter?.addEmptyHeader(getString(R.string.search_no_results))
    }

    override fun getListItemsCount(): Int {
        return mListAdapter?.itemCount ?: 0
    }

    override fun showAllIsLoaded() {
        CenteredToast.show(activity,  R.string.all_artists_are_loaded, Toast.LENGTH_SHORT)
    }

    override fun createAdapter(layoutInflater: LayoutInflater): ItemsAdapter<Artist> {
        return SearchArtistsAdapter(layoutInflater, ContextCompat.getDrawable(activity!!.applicationContext, R.drawable.ic_person_black_24dp_large))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_options, menu)
        menu.removeItem(R.id.action_refresh)
        menu.removeItem(R.id.action_filter)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.open_in_browser -> {
                presenter?.onOpenInBrowser()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        menu.add(0, MENU_SCROBBLES_OF_ARTIST, 0, R.string.scrobbles_of_artist)
        menu.add(0, MENU_OPEN_IN_BROWSER, 1, R.string.open_in_browser)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            MENU_SCROBBLES_OF_ARTIST -> {
                (activity as MainActivity).openScrobblesOfArtistFragment(mListAdapter!!.selectedItem.name)
                true
            }
            MENU_OPEN_IN_BROWSER -> {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(mListAdapter!!.selectedItem.url)))
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun hideKeyboard() {
        InputUtils.hideKeyboard(activity)
    }
}
