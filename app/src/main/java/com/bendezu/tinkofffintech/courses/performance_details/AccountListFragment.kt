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
import kotlinx.android.synthetic.main.fragment_account_list.*

class AccountListFragment : Fragment() {

    private var accountsAdapter: AccountsAdapter? = null
    private lateinit var preferences: SharedPreferences
    private lateinit var repository: StudentsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val db = FintechDatabase.getInstance(requireContext())
        preferences = requireContext().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        repository = StudentsRepository(db.studentDao(), preferences)

        if (accountsAdapter == null) {
            accountsAdapter = AccountsAdapter()
        }
        recycler.apply {
            adapter = accountsAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(ListItemDecoration(context))
            itemAnimator = PopupItemAnimator()
        }

        loadData()
    }

    private fun loadData() {
        repository.callback = object: StudentsRepository.StudentsCallback {
            override fun onResult(students: List<StudentEntity>, fromNetwork: Boolean) {
                showStudents(students)
                if (fromNetwork) swipeRefresh.isRefreshing = false
            }
            override fun onError(t: Throwable) {
                when(t) {
                    is NetworkException -> showNetworkError()
                    is UnauthorizedException -> openAuthorizationActivity()
                }
            }
        }
        repository.getStudents()
    }

    private fun showStudents(students: List<StudentEntity>) {
        accountsAdapter?.data = students
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

    fun query(text: String?) {
        accountsAdapter?.apply { filter.filter(text) }
        checkAccountsCount()
    }

    fun sortAlphabetically() {
        accountsAdapter?.apply {
            val sorted = filteredData.toMutableList()
            sorted.sortBy { it.name }
            filteredData = sorted
        }
    }

    fun sortByMark() {
        accountsAdapter?.apply {
            val sorted = filteredData.toMutableList()
            sorted.sortWith(compareByDescending<StudentEntity>{ it.totalMark }.thenBy { it.name })
            filteredData = sorted
        }
    }

    private fun checkAccountsCount() {
        emptyList.visibility = if (accountsAdapter?.itemCount == 0) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        repository.callback = null
        super.onDestroyView()
    }
}