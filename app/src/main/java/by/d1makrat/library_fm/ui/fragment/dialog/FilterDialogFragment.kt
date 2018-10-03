package by.d1makrat.library_fm.ui.fragment.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import by.d1makrat.library_fm.Constants.*
import by.d1makrat.library_fm.R
import by.d1makrat.library_fm.presenter.fragment.dialog.FilterDialogPresenter
import by.d1makrat.library_fm.ui.CenteredToast
import by.d1makrat.library_fm.view.fragment.FilterDialogFragmentView
import kotlinx.android.synthetic.main.dialog_filter.view.*

class FilterDialogFragment : DialogFragment(), FilterDialogFragmentView {

    private var presenter: FilterDialogPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = FilterDialogPresenter(arguments?.getLong(FILTER_DIALOG_FROM_BUNDLE_KEY) ?: DATE_LONG_DEFAUT_VALUE, arguments?.getLong(FILTER_DIALOG_TO_BUNDLE_KEY) ?: DATE_LONG_DEFAUT_VALUE)

    }

    override fun onCreateDialog(savedInstance: Bundle?): Dialog {
        val activity = activity
        if (activity != null) {
            val alertDialogBuilder = AlertDialog.Builder(activity, R.style.DialogTheme)
            val inflater = activity.layoutInflater
            @SuppressLint("InflateParams") val view = inflater.inflate(R.layout.dialog_filter, null)
            alertDialogBuilder.setView(view)

            val title = TextView(activity.applicationContext)
            title.setText(R.string.filter_dialog_title)
            title.includeFontPadding = true
            title.gravity = Gravity.CENTER
            title.textSize = resources.getDimension(R.dimen.filter_dialog_title_text_size)
            title.setTextColor(Color.BLACK)
            alertDialogBuilder.setCustomTitle(title)

            val datePickerOfStart = view.datePicker_from
            val datePickerOfEnd = view.datePicker_to

            datePickerOfStart.init(presenter?.yearOfStart!!, presenter?.monthOfStart!!, presenter?.dayOfMonthOfStart!!) {
                _, year, monthOfYear, dayOfMonth -> datePickerOfEnd.updateDate(year, monthOfYear, dayOfMonth)
            }

            datePickerOfEnd.init(presenter?.yearOfEnd!!, presenter?.monthOfEnd!!, presenter?.dayOfMonthOfEnd!!, null)

            alertDialogBuilder.setPositiveButton(R.string.filter_dialog_accept) { _, _ ->
                presenter?.onPositiveButtonClicked(datePickerOfStart.year, datePickerOfStart.month, datePickerOfStart.dayOfMonth, datePickerOfEnd.year, datePickerOfEnd.month, datePickerOfEnd.dayOfMonth)
            }

            alertDialogBuilder.setNeutralButton(R.string.filter_dialog_all) { _, _ ->
                presenter?.onNeutralButtonClicked()
            }

            alertDialogBuilder.setNegativeButton(R.string.filter_dialog_cancel) { _, _ ->
                targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_CANCELED, null)
            }

            presenter?.attachView(this)

            return alertDialogBuilder.create()
        }
        else {
            return super.onCreateDialog(savedInstance)
        }
    }

    override fun onStop() {
        presenter?.detachView()

        super.onStop()
    }

    override fun returnToTargetFragment(startOfPeriod: Long, endOfPeriod: Long) {
        val intent = Intent()
        intent.putExtra(FILTER_DIALOG_FROM_BUNDLE_KEY, startOfPeriod)
        intent.putExtra(FILTER_DIALOG_TO_BUNDLE_KEY, endOfPeriod)
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
    }

    override fun showWrongInputMessage() {
        CenteredToast.show(activity, R.string.filter_dialog_wrong_input, Toast.LENGTH_SHORT)
    }
}
