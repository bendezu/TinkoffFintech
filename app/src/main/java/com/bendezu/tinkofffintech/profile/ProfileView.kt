package com.bendezu.tinkofffintech.profile

import com.bendezu.tinkofffintech.network.models.User
import com.hannesdorfmann.mosby3.mvp.MvpView

interface ProfileView: MvpView {
    fun showUserProfile(user: User)
    fun showNetworkError()
    fun openAuthorizationActivity()
    fun setLoading(loading: Boolean)
}