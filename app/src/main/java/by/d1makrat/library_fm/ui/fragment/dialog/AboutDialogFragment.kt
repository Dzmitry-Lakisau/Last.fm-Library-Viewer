package by.d1makrat.library_fm.ui.fragment.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import by.d1makrat.library_fm.R
import kotlinx.android.synthetic.main.dialog_about.view.*

class AboutDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstance: Bundle?): Dialog {
        return if (activity != null) {
            val alertDialogBuilder = AlertDialog.Builder(activity!!, R.style.DialogTheme)

            @SuppressLint("InflateParams") val view = (activity!!).layoutInflater.inflate(R.layout.dialog_about, null)
            alertDialogBuilder.setView(view)

            view.about_text_title.text = getString(R.string.about_app, resources.getString(R.string.app_name))

            alertDialogBuilder.create()
        } else
            super.onCreateDialog(savedInstance)
    }
}
