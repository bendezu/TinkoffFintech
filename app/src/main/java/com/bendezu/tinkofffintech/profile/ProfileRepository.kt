package com.bendezu.tinkofffintech.profile

import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import com.bendezu.tinkofffintech.di.ActivityScope
import com.bendezu.tinkofffintech.getCookie
import com.bendezu.tinkofffintech.getUser
import com.bendezu.tinkofffintech.network.FintechApiService
import com.bendezu.tinkofffintech.network.NetworkException
import com.bendezu.tinkofffintech.network.UnauthorizedException
import com.bendezu.tinkofffintech.network.models.User
import com.bendezu.tinkofffintech.saveUser
import java.io.IOException
import javax.inject.Inject
import kotlin.concurrent.thread

@ActivityScope
class ProfileRepository @Inject constructor(private val preferences: SharedPreferences,
                        private val apiService: FintechApiService) {

    var callback: ProfileCallback? = null

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
                val response = apiService.getUser(cookie).execute()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.status == "Ok") {
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