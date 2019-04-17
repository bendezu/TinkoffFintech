package com.bendezu.tinkofffintech.courses.rating_details

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.bendezu.tinkofffintech.App
import com.bendezu.tinkofffintech.R
import com.bendezu.tinkofffintech.auth.AuthorizationActivity
import com.bendezu.tinkofffintech.courses.performance_details.ListItemDecoration
import com.bendezu.tinkofffintech.data.FintechDatabase
import com.bendezu.tinkofffintech.data.LectureEntity
import com.hannesdorfmann.mosby3.mvp.MvpFragment
import kotlinx.android.synthetic.main.fragment_lectures.*

class LecturesFragment: MvpFragment<LecturesView, LecturesPresenter>(), LecturesView {

    companion object {
        private const val STATE_LOADING = "loading"
    }

    private val lecturesAdapter = LecturesAdapter {
        val fm = fragmentManager ?: return@LecturesAdapter
        fm.beginTransaction()
            .replace(R.id.container, TasksFragment.newInstance(it.id))
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .addToBackStack(null)
            .commit()
    }

    override fun createPresenter(): LecturesPresenter {
        val db = FintechDatabase.getInstance(requireContext())
        return LecturesPresenter(db.lectureDao(), db.taskDao())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_lectures, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            val wasLoading = savedInstanceState.getBoolean(STATE_LOADING)
            setLoading(wasLoading)
        }

        recycler.apply {
            adapter = lecturesAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(ListItemDecoration(requireContext()).apply { margin = 32 })
        }
        swipeRefresh.apply {
            setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorSecondAccent)
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

    override fun showLectures(lectures: List<LectureEntity>) {
        lecturesAdapter.data = lectures
        emptyList.visibility = if (lecturesAdapter.itemCount == 0) View.VISIBLE else View.GONE
    }

    override fun showNetworkError() {
        setLoading(false)
        Toast.makeText(context, R.string.network_error, Toast.LENGTH_SHORT).show()
    }

    override fun openAuthorizationActivity() {
        App.preferences.edit().clear().apply()
        startActivity(Intent(context, AuthorizationActivity::class.java))
        activity?.finish()
    }
}