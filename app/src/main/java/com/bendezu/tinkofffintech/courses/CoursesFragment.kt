package com.bendezu.tinkofffintech.courses

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
import com.bendezu.tinkofffintech.data.entity.StudentEntity
import com.bendezu.tinkofffintech.swipeRefreshColors
import com.hannesdorfmann.mosby3.mvp.MvpFragment
import kotlinx.android.synthetic.main.fragment_courses.*
import javax.inject.Inject

class CoursesFragment: MvpFragment<CoursesView, CoursesPresenter>(), CoursesView {

    interface InjectorProvider {
        fun inject(coursesFragment: CoursesFragment)
    }

    companion object {
        private const val STATE_LOADING = "loading"
    }

    @Inject lateinit var preferences: SharedPreferences
    @Inject lateinit var coursesPresenter: CoursesPresenter
    override fun createPresenter() = coursesPresenter
    private val performanceFragment = PerformanceFragment()
    private val ratingFragment = RatingFragment()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is InjectorProvider)
            context.inject(this)
        else
            throw IllegalArgumentException("$context must implement InjectorProvider")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_courses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.beginTransaction()
            .replace(R.id.performance_container, performanceFragment)
            .replace(R.id.rating_container, ratingFragment)
            .commit()

        view.requestApplyInsets()
        view.setOnApplyWindowInsetsListener { _, insets ->
            statusBarBackground.layoutParams.height = insets.systemWindowInsetTop
            insets.consumeSystemWindowInsets()
        }

        if (savedInstanceState != null) {
            val wasLoading = savedInstanceState.getBoolean(STATE_LOADING)
            setLoading(wasLoading)
        }

        courseDetails.setOnClickListener {  }

        swipeRefresh.setColorSchemeResources(*swipeRefreshColors)
        swipeRefresh.setOnRefreshListener {
            presenter.loadData()
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

    override fun setToolbarTitle(title:String) {
        toolbar.title = title
    }

    override fun setTopStudents(students: List<StudentEntity>) {}

    override fun setRatingStats(stats: RatingStats) {}

    override fun showNetworkError() {
        Toast.makeText(requireContext(), getString(R.string.network_error), Toast.LENGTH_SHORT).show()
    }

    override fun openAuthorizationActivity() {
        preferences.edit().clear().apply()
        startActivity(Intent(context, AuthorizationActivity::class.java))
        activity?.finish()
    }
}