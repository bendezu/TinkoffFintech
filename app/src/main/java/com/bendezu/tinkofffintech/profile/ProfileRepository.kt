package com.bendezu.tinkofffintech.profile

import android.content.SharedPreferences
import com.bendezu.tinkofffintech.di.ActivityScope
import com.bendezu.tinkofffintech.getCookie
import com.bendezu.tinkofffintech.getUser
import com.bendezu.tinkofffintech.network.FintechApiService
import com.bendezu.tinkofffintech.network.models.User
import com.bendezu.tinkofffintech.saveUser
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

@ActivityScope
class ProfileRepository @Inject constructor(private val preferences: SharedPreferences,
                        private val apiService: FintechApiService) {

    fun getUser(): Flowable<User> {
        val cookie = preferences.getCookie()
        val networkSource = apiService.getUserRx(cookie).map { response ->
            if (response.status == "Ok") {
                val user = response.user
                preferences.saveUser(user)
                return@map user
            }
            throw IllegalStateException("status: ${response.status}")
        }
        return Single.concat(Single.fromCallable { preferences.getUser() }, networkSource)
    }

}