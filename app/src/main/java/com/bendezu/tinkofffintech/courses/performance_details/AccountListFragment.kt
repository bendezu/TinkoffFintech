package com.bendezu.tinkofffintech.courses.performance_details

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bendezu.tinkofffintech.R
import com.bendezu.tinkofffintech.SHARED_PREFERENCES_NAME
import com.bendezu.tinkofffintech.auth.AuthorizationActivity
import com.bendezu.tinkofffintech.data.FintechDatabase
import com.bendezu.tinkofffintech.data.StudentEntity
import com.bendezu.tinkofffintech.network.NetworkException
import com.bendezu.tinkofffintech.network.UnauthorizedException
import com.bendezu.tinkofffintech.swipeRefreshColors
import kotlinx.android.synthetic.main.fragment_account_list.*

private const val STATE_QUERY = "query"

class AccountListFragment : Fragment() {

    private val accountsAdapter = AccountsAdapter()
    private lateinit var preferences: SharedPreferences
    private lateinit var repository: StudentsRepository

    private val callback = object : StudentsRepository.StudentsCallback {
        override fun onResult(students: List<StudentEntity>, shouldStopLoading: Boolean) {
            showStudents(students)
            if (shouldStopLoading)
                swipeRefresh.isRefreshing = false
            else
                if (students.isEmpty()) swipeRefresh.isRefreshing = true
        }

        override fun onError(t: Throwable) {
            when (t) {
                is NetworkException -> showNetworkError()
                is UnauthorizedException -> openAuthorizationActivity()
            }
        }
    }

    var query: String = ""
        set(value) {
            field = value
            accountsAdapter.filter(value)
            checkAccountsCount()
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val db = FintechDatabase.getInstance(requireContext())
        preferences = requireContext().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        repository = StudentsRepository(db.studentDao(), preferences, callback)

        if (savedInstanceState != null) {
            query = savedInstanceState.getString(STATE_QUERY).orEmpty()
        }

        recycler.apply {
            adapter = accountsAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(ListItemDecoration(context))
            itemAnimator = PopupItemAnimator()
        }
        swipeRefresh.apply {
            setColorSchemeResources(*swipeRefreshColors)
            setOnRefreshListener { loadData() }
        }
        loadData()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(STATE_QUERY, query)
    }

    private fun loadData() {
        repository.getStudents()
    }

    private fun showStudents(students: List<StudentEntity>) {
        accountsAdapter.setNewData(students, query)
        checkAccountsCount()
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

    fun sortAlphabetically() {
        accountsAdapter.apply {
            val sorted = filteredData.toMutableList()
            sorted.sortBy { it.name }
            filteredData = sorted
        }
        recycler.scrollToPosition(0)
    }

    fun sortByMark() {
        accountsAdapter.apply {
            val sorted = filteredData.toMutableList()
            sorted.sortWith(compareByDescending<StudentEntity> { it.totalMark }.thenBy { it.name })
            filteredData = sorted
        }
        recycler.scrollToPosition(0)
    }

    private fun checkAccountsCount() {
        emptyList.visibility = if (accountsAdapter.itemCount == 0) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        repository.callback = null
        super.onDestroyView()
    }
}