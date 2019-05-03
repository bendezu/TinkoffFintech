package com.bendezu.tinkofffintech.auth

import android.content.Intent
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.bendezu.tinkofffintech.MainActivity
import com.bendezu.tinkofffintech.R
import com.bendezu.tinkofffintech.di.Injector
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import kotlinx.android.synthetic.main.activity_authorization.*
import javax.inject.Inject

class AuthorizationActivity : MvpActivity<AuthView, AuthPresenter>(), AuthView {

    companion object {
        private const val STATE_LOADING = "loading"
        private const val STATE_LOGIN_ERROR_VISIBILITY = "error_visibility"
        private const val STATE_LOGIN_ERROR = "error"
    }

    @Inject lateinit var authPresenter: AuthPresenter
    override fun createPresenter() = authPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        Injector.authActivityComponent().inject(this)
        setTheme(R.style.AppTheme_Activity_AuthTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)

        if (savedInstanceState != null) {
            val wasLoading = savedInstanceState.getBoolean(STATE_LOADING)
            val hadError = savedInstanceState.getBoolean(STATE_LOGIN_ERROR_VISIBILITY)
            if (wasLoading) setLoading(wasLoading)
            if (hadError) {
                errorTextView.visibility = View.VISIBLE
                errorTextView.text = savedInstanceState.getString(STATE_LOGIN_ERROR)
            }
        }

        presenter.verifyCookie()

        logInButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            presenter.logIn(email, password)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.apply {
            putBoolean(STATE_LOADING, progressBar.isVisible)
            putBoolean(STATE_LOGIN_ERROR_VISIBILITY, errorTextView.isVisible)
            putString(STATE_LOGIN_ERROR, errorTextView.text.toString())
        }
        super.onSaveInstanceState(outState)
    }

    override fun showErrorMessage() {
        errorTextView.setText(R.string.wrong_email_or_password)
        errorTextView.visibility = View.VISIBLE
    }

    override fun showWrongEmailMessage() {
        errorTextView.setText(R.string.wrong_email)
        errorTextView.visibility = View.VISIBLE
    }

    override fun showEmptyPasswordMessage() {
        errorTextView.setText(R.string.empty_password)
        errorTextView.visibility = View.VISIBLE
    }

    override fun setLoading(loading: Boolean) {
        TransitionManager.beginDelayedTransition(root)
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