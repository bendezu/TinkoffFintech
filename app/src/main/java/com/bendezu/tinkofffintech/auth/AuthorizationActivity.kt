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
import com.bendezu.tinkofffintech.network.FintechApiService
import com.bendezu.tinkofffintech.network.User
import com.bendezu.tinkofffintech.network.UserCredential
import kotlinx.android.synthetic.main.activity_authorization.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AuthorizationActivity : AppCompatActivity(), Callback<User> {

    companion object {
        const val TAG = "AuthorizationActivity"
    }

    lateinit var apiService: FintechApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)

        val sharedPrefs = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val cookie = sharedPrefs.getString(PREF_COOKIE, null)
        val expires = sharedPrefs.getString(PREF_EXPIRES, null)
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

        val retrofit = Retrofit.Builder()
            .baseUrl(FintechApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create()

        logInButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            apiService.signIn(UserCredential(email, password)).enqueue(this)
        }
    }

    private fun showErrorMessage() {
        errorTextView.visibility = View.VISIBLE
    }

    override fun onFailure(call: Call<User>, t: Throwable) {
        Toast.makeText(this, getString(R.string.network_error), Toast.LENGTH_SHORT).show()
    }

    override fun onResponse(call: Call<User>, response: Response<User>) {
        if (response.isSuccessful) {
            val setCookie = response.headers().get("Set-Cookie")
            if (setCookie != null) {
                val set = setCookie.split("; ")
                val cookie = set.find { it.startsWith("anygen") }
                val expires = set.find { it.startsWith("expires") }
                if (cookie != null && expires != null) {
                    saveCookieToSharedPref(cookie, expires.substringAfter('='))
                    openMainActivity()
                }
            }
        } else {
            showErrorMessage()
        }
    }

    private fun saveCookieToSharedPref(cookie: String, expires: String) {
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
        SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss zzz", Locale.US)
            .apply { timeZone = TimeZone.getTimeZone("GMT") }
            .parse(dateStr) ?: throw ParseException(dateStr, 1)
}