package com.bendezu.tinkofffintech.courses.rating_details

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.bendezu.tinkofffintech.R
import com.bendezu.tinkofffintech.auth.AuthorizationActivity
import com.bendezu.tinkofffintech.courses.performance_details.ListItemDecoration
import com.bendezu.tinkofffintech.data.entity.LectureEntity
import com.bendezu.tinkofffintech.swipeRefreshColors
import com.hannesdorfmann.mosby3.mvp.MvpFragment
import kotlinx.android.synthetic.main.fragment_lectures.*
import javax.inject.Inject

class LecturesFragment: MvpFragment<LecturesView, LecturesPresenter>(), LecturesView {

    interface Listener {
        fun inject(lecturesFragment: LecturesFragment)
        fun setToolbarTitle(title: String)
    }

    companion object {
        private const val STATE_LOADING = "loading"
    }

    @Inject lateinit var lecturesAdapter: LecturesAdapter
    @Inject lateinit var preferences: SharedPreferences
    @Inject lateinit var lecturesPresenter: LecturesPresenter
    override fun createPresenter() = lecturesPresenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Listener) {
            context.inject(this)
        } else
            throw IllegalStateException("$context must implement InjectorProvider")
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

        lecturesAdapter.listener = {
            (activity as Listener).setToolbarTitle(it.title)
            fragmentManager?.apply {
                beginTransaction()
                    .replace(R.id.container, TasksFragment.newInstance(it.id))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null)
                    .commit()
            }
        }

        recycler.apply {
            adapter = lecturesAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(ListItemDecoration(requireContext()).apply { margin = 32 })
        }
        swipeRefresh.apply {
            setColorSchemeResources(*swipeRefreshColors)
            setOnRefreshListener{ presenter.loadData() }
        }
        presenter.loadData()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as Listener).setToolbarTitle(requireContext().getString(R.string.lectures))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (isAdded)
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
        Toast.makeText(context, R.string.network_error, Toast.LENGTH_SHORT).show()
    }

    override fun openAuthorizationActivity() {
        preferences.edit().clear().apply()
        startActivity(Intent(context, AuthorizationActivity::class.java))
        activity?.finish()
    }
}