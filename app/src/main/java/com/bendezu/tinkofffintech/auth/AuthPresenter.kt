package com.bendezu.tinkofffintech.auth

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.bendezu.tinkofffintech.*
import com.bendezu.tinkofffintech.network.FintechApiService
import com.bendezu.tinkofffintech.network.User
import com.bendezu.tinkofffintech.network.UserCredential
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AuthPresenter(private val preferences: SharedPreferences = App.preferences,
                    private val apiService: FintechApiService = App.apiService) :
    MvpBasePresenter<AuthView>(), Callback<User> {

    fun verifyCookie() {
        val cookie = preferences.getString(PREF_COOKIE, null)
        val expires = preferences.getString(PREF_EXPIRES, null)
        if (cookie != null && expires != null) {
            try {
                val expiresDate = parseExpiresDateString(expires)
                val now = Date(System.currentTimeMillis())
                if (now < expiresDate) {
                    ifViewAttached { it.openMainActivity() }
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
    }

    fun logIn(email: String, password: String) {
        apiService.signIn(UserCredential(email, password)).enqueue(this)
        ifViewAttached { it.setLoading(true) }
    }

    override fun onFailure(call: Call<User>, t: Throwable) {
        ifViewAttached {
            it.setLoading(false)
            it.showNetworkError()
        }
    }

    override fun onResponse(call: Call<User>, response: Response<User>) {
        ifViewAttached { it.setLoading(false) }
        if (response.isSuccessful) {
            val setCookie = response.headers().get(COOKIE_HEADER)
            if (setCookie != null) {
                val set = setCookie.split("; ")
                val cookie = set.find { it.startsWith(COOKIE_ANYGEN) }
                val expires = set.find { it.startsWith(COOKIE_EXPIRES) }
                if (cookie != null && expires != null) {
                    saveCookieToPrefs(cookie, expires.substringAfter('='))
                    ifViewAttached { it.openMainActivity() }
                }
            }
        } else {
            ifViewAttached { it.showErrorMessage() }
        }
    }

    private fun saveCookieToPrefs(cookie: String, expires: String) {
        Log.d(AuthorizationActivity.TAG, "Cookie: $cookie")
        Log.d(AuthorizationActivity.TAG, "Expires: $expires")
        preferences.edit {
            putString(PREF_COOKIE, cookie)
            putString(PREF_EXPIRES, expires)
        }
    }

    private fun parseExpiresDateString(dateStr: String) =
        SimpleDateFormat(EXPIRES_DATE_FORMAT, Locale.US)
            .apply { timeZone = TimeZone.getTimeZone(GMT_TIME_ZONE) }
            .parse(dateStr) ?: throw ParseException(dateStr, 1)
}