package com.bendezu.tinkofffintech.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bendezu.tinkofffintech.R
import com.bendezu.tinkofffintech.auth.AuthorizationActivity
import com.bendezu.tinkofffintech.network.models.User
import com.bendezu.tinkofffintech.swipeRefreshColors
import com.bumptech.glide.Glide
import com.hannesdorfmann.mosby3.mvp.MvpFragment
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

class ProfileFragment: MvpFragment<ProfileView, ProfilePresenter>(), ProfileView {

    interface InjectorProvider {
        fun inject(profileFragment: ProfileFragment)
    }

    companion object {
        private const val STATE_LOADING = "loading"
        private const val AVATAR_URL_BASE = "https://fintech.tinkoff.ru"
    }

    @Inject lateinit var preferences: SharedPreferences
    @Inject lateinit var profilePresenter: ProfilePresenter
    override fun createPresenter() = profilePresenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is InjectorProvider)
            context.inject(this)
        else
            throw IllegalStateException("$context must implement InjectorProvider")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.requestApplyInsets()
        view.setOnApplyWindowInsetsListener { _, insets ->
            val appBarHeight = requireContext().resources.getDimension(R.dimen.avatar_app_bar_height).toInt()
            appBar.layoutParams.height = appBarHeight + insets.systemWindowInsetTop
            (logOutButton.layoutParams as ViewGroup.MarginLayoutParams).topMargin = insets.systemWindowInsetTop
            insets.consumeSystemWindowInsets()
        }

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
        if (user.avatar == null) {
            //placeholder
        } else {
            val avatarUrl = AVATAR_URL_BASE + user.avatar
            Glide.with(this).load(avatarUrl).into(avatarImageView)
        }
        nameTextView.text = "${user.firstname} ${user.lastname} ${user.middlename}"
        profileEmailTextView.text = user.email

        if (user.description == null) {
            descriptionBlock.visibility = View.GONE
        } else {
            descriptionTextView.text = user.description
            val age = presenter.getAgeByBirthday(user.birthday)
            nameAgeTextView.text = requireContext().resources.getQuantityString(
                R.plurals.nameAge, age, user.firstname, user.lastname, age)
        }

        phoneTextView.text = user.phoneNumber
        emailTextView.text = user.email
        regionTextView.text = user.region

        schoolTextView.text = user.school
        if (user.schoolGraduation != null)
            schoolGraduationTextView.text = requireContext().getString(R.string.graduation_year, user.schoolGraduation)
        universityTextView.text = user.university
        if (user.universityGraduation != null)
            universityGraduationTextView.text = requireContext().getString(R.string.graduation_year, user.universityGraduation)
        departmentTextView.text = user.department

        currentWorkTextView.text = user.currentWork
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