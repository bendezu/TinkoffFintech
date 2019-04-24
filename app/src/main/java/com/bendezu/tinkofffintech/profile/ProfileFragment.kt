package com.bendezu.tinkofffintech.profile

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bendezu.tinkofffintech.R
import com.bendezu.tinkofffintech.auth.AuthorizationActivity
import com.bendezu.tinkofffintech.di.Injector
import com.bendezu.tinkofffintech.getAvatarColor
import com.bendezu.tinkofffintech.getInitials
import com.bendezu.tinkofffintech.network.User
import com.bendezu.tinkofffintech.swipeRefreshColors
import com.bumptech.glide.Glide
import com.hannesdorfmann.mosby3.mvp.MvpFragment
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

class ProfileFragment: MvpFragment<ProfileView, ProfilePresenter>(), ProfileView {

    companion object {
        private const val STATE_LOADING = "loading"
        private const val AVATAR_URL_BASE = "https://fintech.tinkoff.ru"
    }

    @Inject lateinit var preferences: SharedPreferences
    @Inject lateinit var profilePresenter: ProfilePresenter
    override fun createPresenter() = profilePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        Injector.profileFragmentComponent().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            val wasLoading = savedInstanceState.getBoolean(STATE_LOADING)
            swipeRefresh.isRefreshing = wasLoading
        }

        logOutButton.setOnClickListener { openAuthorizationActivity() }

        swipeRefresh.apply {
            setColorSchemeResources(*swipeRefreshColors)
            setOnRefreshListener{ presenter.loadData() }
        }
        presenter.loadData()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(STATE_LOADING, swipeRefresh.isRefreshing)
        super.onSaveInstanceState(outState)
    }

    override fun setLoading(loading: Boolean) {
        swipeRefresh.isRefreshing = loading
    }

    override fun showUserProfile(user: User) {
        firstNameTextView.text = user.firstname
        secondNameTextView.text = user.lastname
        patronymicTextView.text = user.middlename
        if (user.avatar == null) {
            val initials = "${user.firstname} ${user.lastname}".getInitials()
            avatarImageView.setImageDrawable(ColorDrawable(initials.getAvatarColor()))
            avatarImageView.initials = initials
        } else {
            val avatarUrl = AVATAR_URL_BASE + user.avatar
            Glide.with(this).load(avatarUrl).into(avatarImageView)
        }
    }

    override fun showNetworkError() {
        Toast.makeText(requireContext(), getString(R.string.network_error), Toast.LENGTH_SHORT).show()
    }

    override fun openAuthorizationActivity() {
        preferences.edit().clear().apply()
        startActivity(Intent(context, AuthorizationActivity::class.java))
        activity?.finish()
    }
}