package by.d1makrat.library_fm.ui.activity

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import by.d1makrat.library_fm.AppContext
import by.d1makrat.library_fm.R
import by.d1makrat.library_fm.presenter.activity.PreferencePresenter
import by.d1makrat.library_fm.ui.CenteredToast
import by.d1makrat.library_fm.utils.InputUtils.hideKeyboard
import by.d1makrat.library_fm.view.activity.PreferenceView
import kotlinx.android.synthetic.main.activity_preferences.*

class PreferenceActivity : Activity(), PreferenceView {

    var presenter = PreferencePresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_preferences)

        presenter.attachView(this)

        set_limit_editText.hint = AppContext.getInstance().limit.toString()

        set_limit_editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                set_limit_button.isEnabled = s.isNotEmpty()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        set_limit_button.setOnClickListener { presenter.onSetLimitButtonClicked(set_limit_editText.text.toString()) }

        clear_image_cache_textView.setOnClickListener { presenter.onClearImageCacheButtonClicked() }

        drop_database_textView.setOnClickListener { presenter.onDropDatabaseButtonClicked() }

        layout_preferences.setOnClickListener{ hideKeyboard(this) }
    }

    override fun onStop() {
        super.onStop()

        presenter.detachView()
    }

    override fun showLimitMoreThan200Message() {
        hideKeyboard(this)

        CenteredToast.show(applicationContext, R.string.limit_more_than_200, Toast.LENGTH_LONG)
    }

    override fun showLimitSetOKMessage() {
        hideKeyboard(this)

        CenteredToast.show(applicationContext, R.string.limit_has_been_set, Toast.LENGTH_SHORT)

        set_limit_editText.hint = AppContext.getInstance().limit.toString()
    }

    override fun showLimitMustBeBetween() {
        CenteredToast.show(applicationContext, R.string.limit_must_be_between, Toast.LENGTH_SHORT)
    }

    override fun showNonnumericalInputMessage() {
        CenteredToast.show(applicationContext, R.string.limit_nonnumerical_input, Toast.LENGTH_SHORT)
    }

    override fun showOK() {
        CenteredToast.show(applicationContext, R.string.dialog_ok, Toast.LENGTH_SHORT)
    }

    override fun showUnableToClearImageCache() {
        CenteredToast.show(applicationContext, R.string.unable_to_clear_cache, Toast.LENGTH_SHORT)
    }

    override fun showUnableToDropDatabaseMessage() {
        CenteredToast.show(applicationContext, R.string.unable_to_drop_database, Toast.LENGTH_SHORT)
    }
}
