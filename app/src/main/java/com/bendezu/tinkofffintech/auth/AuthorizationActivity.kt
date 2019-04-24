package com.bendezu.tinkofffintech.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.bendezu.tinkofffintech.App
import com.bendezu.tinkofffintech.MainActivity
import com.bendezu.tinkofffintech.R
import com.bendezu.tinkofffintech.di.components.DaggerAuthorizationActivityComponent
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import kotlinx.android.synthetic.main.activity_authorization.*
import javax.inject.Inject

class AuthorizationActivity : MvpActivity<AuthView, AuthPresenter>(), AuthView {

    companion object {
        const val TAG = "AuthorizationActivity"
        private const val STATE_LOADING = "loading"
        private const val STATE_LOGIN_ERROR = "error"
    }

    @Inject lateinit var authPresenter: AuthPresenter
    override fun createPresenter() = authPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerAuthorizationActivityComponent.builder()
            .appComponent(App.component)
            .build().inject(this)

        setTheme(R.style.AppTheme_ActivityTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)

        if (savedInstanceState != null) {
            val wasLoading = savedInstanceState.getBoolean(STATE_LOADING)
            val hadError = savedInstanceState.getBoolean(STATE_LOGIN_ERROR)
            if (wasLoading) setLoading(wasLoading)
            if (hadError) showErrorMessage()
        }

        presenter.verifyCookie()

        logInButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            presenter.logIn(email, password)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(STATE_LOADING, progressBar.isVisible)
        outState.putBoolean(STATE_LOGIN_ERROR, errorTextView.isVisible)
        super.onSaveInstanceState(outState)
    }

    override fun showErrorMessage() {
        errorTextView?.visibility = View.VISIBLE
    }
    override fun setLoading(loading: Boolean) {
        progressBar?.visibility = if (loading) View.VISIBLE else View.INVISIBLE
        logInButton?.isEnabled = !loading
    }

    override fun openMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun showNetworkError() {
        Toast.makeText(this, getString(R.string.network_error), Toast.LENGTH_SHORT).show()
    }
}