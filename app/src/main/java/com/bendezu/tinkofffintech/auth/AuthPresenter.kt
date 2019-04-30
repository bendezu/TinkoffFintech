package com.bendezu.tinkofffintech.auth

import android.content.SharedPreferences
import com.bendezu.tinkofffintech.*
import com.bendezu.tinkofffintech.di.ActivityScope
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
import javax.inject.Inject

@ActivityScope
class AuthPresenter @Inject constructor(private val preferences: SharedPreferences,
                                        private val apiService: FintechApiService) :
    MvpBasePresenter<AuthView>(), Callback<User> {

    fun verifyCookie() {
        val cookie = preferences.getCookie()
        val expires = preferences.getExpires()
        if (cookie.isNotEmpty() && expires.isNotEmpty()) {
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
            response.body()?.let { preferences.saveUser(it) }
            val setCookie = response.headers().get(COOKIE_HEADER)
            if (setCookie != null) {
                val set = setCookie.split("; ")
                val cookie = set.find { it.startsWith(COOKIE_ANYGEN) }
                val expires = set.find { it.startsWith(COOKIE_EXPIRES) }
                if (cookie != null && expires != null) {
                    preferences.saveCookie(cookie, expires.substringAfter('='))
                    ifViewAttached { it.openMainActivity() }
                }
            }
        } else {
            ifViewAttached { it.showErrorMessage() }
        }
    }

    private fun parseExpiresDateString(dateStr: String) =
        SimpleDateFormat(EXPIRES_DATE_FORMAT, Locale.US)
            .apply { timeZone = TimeZone.getTimeZone(GMT_TIME_ZONE) }
            .parse(dateStr) ?: throw ParseException(dateStr, 1)
}