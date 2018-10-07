package by.d1makrat.library_fm.adapter.pages

import android.os.Bundle
import android.support.v4.app.FragmentManager
import by.d1makrat.library_fm.Constants.PERIOD_KEY
import by.d1makrat.library_fm.model.Period
import by.d1makrat.library_fm.ui.fragment.top.TopArtistsFragment

class TabTopArtistsAdapter(fragmentManager: FragmentManager) : TabTopAdapter(fragmentManager) {

    override fun getItem(position: Int): TopArtistsFragment {
        val bundle = Bundle()
        bundle.putString(PERIOD_KEY, Period().getValueForApi(position))
        val fragment = TopArtistsFragment()
        fragment.arguments = bundle
        return fragment
    }
}
