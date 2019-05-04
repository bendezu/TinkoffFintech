package com.bendezu.tinkofffintech.courses.course_details

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import com.bendezu.tinkofffintech.R
import com.bendezu.tinkofffintech.auth.AuthorizationActivity
import com.bendezu.tinkofffintech.di.Injector
import com.bendezu.tinkofffintech.network.models.CourseDetails
import com.bendezu.tinkofffintech.swipeRefreshColors
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import kotlinx.android.synthetic.main.activity_course_detail.*
import javax.inject.Inject

class CourseDetailActivity : MvpActivity<CourseDetailsView, CourseDetailPresenter>(), CourseDetailsView {

    companion object {
        private const val STATE_LOADING = "loading"
        private const val STATE_TITLE = "title"
    }

    @Inject lateinit var preferences: SharedPreferences
    @Inject lateinit var courseDetailPresenter: CourseDetailPresenter
    override fun createPresenter() = courseDetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        Injector.courseDetailComponent().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setOnApplyWindowInsetsListener { _, insets ->
            statusBarBackground.layoutParams.height = insets.systemWindowInsetTop
            insets.consumeSystemWindowInsets()
        }

        if (savedInstanceState != null) {
            setLoading(savedInstanceState.getBoolean(STATE_LOADING))
            toolbar.title = savedInstanceState.getString(STATE_TITLE)
        }

        swipeRefresh.setColorSchemeResources(*swipeRefreshColors)
        swipeRefresh.setOnRefreshListener {
            presenter.loadFromNetwork()
        }

        presenter.loadData()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(STATE_LOADING, swipeRefresh.isRefreshing)
        outState.putString(STATE_TITLE, toolbar.title.toString())
        super.onSaveInstanceState(outState)
    }

    override fun showData(courseDetails: CourseDetails) {
        webView.loadData(courseDetails.html, "text/html", "base64")
        toolbar.title = courseDetails.title
    }

    override fun showNetworkError() {
        Toast.makeText(this, getString(R.string.network_error), Toast.LENGTH_SHORT).show()
    }

    override fun setLoading(loading: Boolean) {
        swipeRefresh.isRefreshing = loading
    }

    override fun openAuthorizationActivity() {
        preferences.edit().clear().apply()
        startActivity(Intent(this, AuthorizationActivity::class.java))
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}