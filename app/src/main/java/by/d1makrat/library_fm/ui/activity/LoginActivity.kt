package by.d1makrat.library_fm.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import by.d1makrat.library_fm.R
import by.d1makrat.library_fm.presenter.activity.LoginPresenter
import by.d1makrat.library_fm.ui.CenteredToast
import by.d1makrat.library_fm.utils.InputUtils.hideKeyboard
import by.d1makrat.library_fm.view.activity.LoginView
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity(), LoginView {

    private val presenter = LoginPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        presenter.attachView(this)

        presenter.onEnterInApp()
    }

    override fun onStop() {
        presenter.detachView()

        super.onStop()
    }

    override fun createListeners() {
        button_enter.setOnClickListener {
            hideKeyboard(this)
            presenter.onEnterButtonClick(edit_username.text.toString(), edit_password.text.toString())
        }
        sign_in_page.setOnClickListener { hideKeyboard(this) }
        edit_username.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                button_enter.isEnabled = edit_password.text.isNotEmpty() && s.isNotEmpty()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
        edit_password.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                button_enter.isEnabled = edit_username.text.isNotEmpty() && s.isNotEmpty()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    override fun showProgressBar() {
        progressBar_login.visibility = View.VISIBLE
        edit_username.visibility = View.INVISIBLE
        edit_password.visibility = View.INVISIBLE
        button_enter.visibility = View.INVISIBLE
    }

    override fun hideProgressBar() {
        progressBar_login.visibility = View.INVISIBLE
        edit_username.visibility = View.VISIBLE
        edit_password.visibility = View.VISIBLE
        button_enter.visibility = View.VISIBLE
    }

    override fun showErrorMessage(message: String) {
        CenteredToast.show(this, message, Toast.LENGTH_SHORT)
    }

    override fun showNoConnectionMessage() {
        CenteredToast.show(applicationContext, R.string.network_is_not_connected, Toast.LENGTH_SHORT)
    }

    override fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
