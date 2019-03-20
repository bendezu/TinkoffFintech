package com.bendezu.tinkofffintech.courses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bendezu.tinkofffintech.R
import kotlinx.android.synthetic.main.fragment_courses.*

class CoursesFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_courses, container, false)
    }

    private val performanceFragment = PerformanceFragment()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        childFragmentManager.beginTransaction()
            .replace(R.id.performance_container, performanceFragment)
            .replace(R.id.rating_container, RatingFragment())
            .replace(R.id.completed_courses_container, CompletedCoursesFragment())
            .commit()

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorSecondAccent)
        swipeRefresh.setOnRefreshListener {
            performanceFragment.updateAccounts()
            swipeRefresh.isRefreshing = false
        }
    }
}