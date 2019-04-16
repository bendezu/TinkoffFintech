package com.bendezu.tinkofffintech.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.bendezu.tinkofffintech.*
import com.bendezu.tinkofffintech.network.User
import com.bendezu.tinkofffintech.network.UserCredential
import kotlinx.android.synthetic.main.activity_authorization.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AuthorizationActivity : AppCompatActivity(), Callback<User> {

    companion object {
        const val TAG = "AuthorizationActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_ActivityTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)

        val preferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val cookie = preferences.getString(PREF_COOKIE, null)
        val expires = preferences.getString(PREF_EXPIRES, null)
        if (cookie != null && expires != null) {
            try {
                val expiresDate = parseExpiresDateString(expires)
                val now = Date(System.currentTimeMillis())
                if (now < expiresDate) {
                    openMainActivity()
                }
            } catch (ex: ParseException) {
                ex.printStackTrace()
            }
        }

        logInButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            App.apiService.signIn(UserCredential(email, password)).enqueue(this)
            setLoading(true)
        }
    }

    private fun showErrorMessage() {
        errorTextView?.visibility = View.VISIBLE
    }
    private fun setLoading(isLoading: Boolean) {
        progressBar?.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
        logInButton?.isEnabled = !isLoading
    }

    override fun onFailure(call: Call<User>, t: Throwable) {
        setLoading(false)
        Toast.makeText(this, getString(R.string.network_error), Toast.LENGTH_SHORT).show()
    }

    override fun onResponse(call: Call<User>, response: Response<User>) {
        setLoading(false)
        if (response.isSuccessful) {
            val setCookie = response.headers().get(COOKIE_HEADER)
            if (setCookie != null) {
                val set = setCookie.split("; ")
                val cookie = set.find { it.startsWith(COOKIE_ANYGEN) }
                val expires = set.find { it.startsWith(COOKIE_EXPIRES) }
                if (cookie != null && expires != null) {
                    saveCookieToPrefs(cookie, expires.substringAfter('='))
                    openMainActivity()
                }
            }
        } else {
            showErrorMessage()
        }
    }

    private fun saveCookieToPrefs(cookie: String, expires: String) {
        Log.d(TAG, "Cookie: $cookie")
        Log.d(TAG, "Expires: $expires")
        getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
            ?.edit {
                putString(PREF_COOKIE, cookie)
                putString(PREF_EXPIRES, expires)
            }
    }

    private fun openMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun parseExpiresDateString(dateStr: String): Date =
        SimpleDateFormat(EXPIRES_DATE_FORMAT, Locale.US)
            .apply { timeZone = TimeZone.getTimeZone(GMT_TIME_ZONE) }
            .parse(dateStr) ?: throw ParseException(dateStr, 1)
}