package com.bendezu.tinkofffintech.profile

import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import com.bendezu.tinkofffintech.App
import com.bendezu.tinkofffintech.getCookie
import com.bendezu.tinkofffintech.getUser
import com.bendezu.tinkofffintech.network.NetworkException
import com.bendezu.tinkofffintech.network.UnauthorizedException
import com.bendezu.tinkofffintech.network.User
import com.bendezu.tinkofffintech.saveUser
import java.io.IOException
import kotlin.concurrent.thread

class ProfileRepository(private val preferences: SharedPreferences,
                        var callback: ProfileCallback? = null) {

    interface ProfileCallback {
        fun onResult(user: User, shouldStopLoading: Boolean = false)
        fun onError(t: Throwable)
    }

    private val uiHandler = Handler(Looper.getMainLooper())

    fun getUser() {
        thread {
            val prefUser = preferences.getUser()
            uiHandler.post{ callback?.onResult(prefUser) }

            val cookie = preferences.getCookie()
            try {
                val response = App.apiService.getUser(cookie).execute()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        uiHandler.post{ callback?.onResult(body.user, true) }
                        preferences.saveUser(body.user)
                    }
                } else {
                    uiHandler.post{ callback?.onError(UnauthorizedException()) }
                }
            } catch (e: IOException) {
                uiHandler.post{ callback?.onError(NetworkException()) }
            }
        }
    }

}