package com.bendezu.tinkofffintech.courses.rating_details

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.bendezu.tinkofffintech.R
import com.bendezu.tinkofffintech.SHARED_PREFERENCES_NAME
import com.bendezu.tinkofffintech.auth.AuthorizationActivity
import com.bendezu.tinkofffintech.courses.performance_details.ListItemDecoration
import com.bendezu.tinkofffintech.data.FintechDatabase
import com.bendezu.tinkofffintech.data.LectureEntity
import com.bendezu.tinkofffintech.network.NetworkException
import com.bendezu.tinkofffintech.network.UnauthorizedException
import kotlinx.android.synthetic.main.fragment_lectures.*

class LecturesFragment: Fragment() {

    private val lecturesAdapter = LecturesAdapter {
        val fm = fragmentManager ?: return@LecturesAdapter
        fm.beginTransaction()
            .replace(R.id.container, TasksFragment.newInstance(it.id))
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .addToBackStack(null)
            .commit()
    }
    private lateinit var preferences: SharedPreferences
    private lateinit var repository: HomeworksRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_lectures, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val db = FintechDatabase.getInstance(requireContext())
        preferences = requireActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        repository = HomeworksRepository(db.lectureDao(), db.taskDao(), preferences)

        recycler.apply {
            adapter = lecturesAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(ListItemDecoration(requireContext()).apply { margin = 32 })
        }
        swipeRefresh.apply {
            setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorSecondAccent)
            setOnRefreshListener{ loadData() }
        }
        loadData()
    }

    private fun loadData() {
        repository.callback = object: HomeworksRepository.LecturesCallback {
            override fun onResult(lectures: List<LectureEntity>, fromNetwork: Boolean) {
                showLectures(lectures)
                if (fromNetwork) swipeRefresh.isRefreshing = false
            }
            override fun onError(t: Throwable) {
                when(t) {
                    is NetworkException -> showNetworkError()
                    is UnauthorizedException -> openAuthorizationActivity()
                }
            }
        }
        repository.getLectures()
    }

    private fun showLectures(lectures: List<LectureEntity>) {
        lecturesAdapter.data = lectures
        emptyList.visibility = if (lecturesAdapter.itemCount == 0) View.VISIBLE else View.GONE
    }

    private fun showNetworkError() {
        swipeRefresh.isRefreshing = false
        Toast.makeText(context, R.string.network_error, Toast.LENGTH_SHORT).show()
    }

    private fun openAuthorizationActivity() {
        preferences.edit().clear().apply()
        startActivity(Intent(context, AuthorizationActivity::class.java))
        activity?.finish()
    }

    override fun onDestroyView() {
        repository.callback = null
        super.onDestroyView()
    }
}