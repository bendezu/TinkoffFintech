package com.bendezu.tinkofffintech.auth

import com.hannesdorfmann.mosby3.mvp.MvpView

interface AuthView: MvpView {
    fun setLoading(loading: Boolean)
    fun openMainActivity()
    fun showErrorMessage()
    fun showNetworkError()
    fun showWrongEmailMessage()
    fun showEmptyPasswordMessage()
}