package by.d1makrat.library_fm.ui.fragment.tabTop

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.*
import by.d1makrat.library_fm.R
import by.d1makrat.library_fm.adapter.pages.TabTopAdapter

abstract class TabTopItemsFragment<A: TabTopAdapter>: Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)

        val view = inflater.inflate(R.layout.viewpager, container, false)
        val viewPager: ViewPager = view.findViewById(R.id.viewpager)
        viewPager.adapter = createAdapter()

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu!!.clear()
        inflater!!.inflate(R.menu.menu_options, menu)
        menu.removeItem(R.id.action_filter)
    }

    abstract fun createAdapter(): A?
}
