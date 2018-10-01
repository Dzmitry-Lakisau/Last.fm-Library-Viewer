package by.d1makrat.library_fm.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.R
import by.d1makrat.library_fm.image_loader.Malevich
import by.d1makrat.library_fm.ui.activity.MainActivity
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.fragment_start.*
import kotlinx.android.synthetic.main.fragment_start.view.*

class StartFragment: Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_start, container, false)

        rootView.adView.loadAd(AdRequest.Builder().build())

        val user = AppContext.getInstance().user
        Malevich.INSTANCE.load(user.avatarUrl).onError(resources.getDrawable(R.drawable.img_app_logo_large)).into(user_avatar_start_screen)

        rootView.hello_textView.text = getString(R.string.hello_message, user.username, user.playcount, user.registered)

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu!!.clear()
        inflater!!.inflate(R.menu.menu_about_app, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.about -> {
                (activity as MainActivity).showAboutDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
