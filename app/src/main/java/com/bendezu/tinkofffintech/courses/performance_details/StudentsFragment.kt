package com.bendezu.tinkofffintech.courses.performance_details

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bendezu.tinkofffintech.R
import com.bendezu.tinkofffintech.auth.AuthorizationActivity
import com.bendezu.tinkofffintech.data.StudentEntity
import com.bendezu.tinkofffintech.swipeRefreshColors
import com.hannesdorfmann.mosby3.mvp.MvpFragment
import kotlinx.android.synthetic.main.fragment_account_list.*
import javax.inject.Inject

object SortType {
    const val NONE = "none"
    const val ALPHABETICALLY = "alphabetically"
    const val BY_MARK = "by_mark"
}

class StudentsFragment : MvpFragment<StudentsView, StudentsPresenter>(), StudentsView {

    interface InjectorProvider {
        fun inject(studentsFragment: StudentsFragment)
    }

    companion object {
        private const val STATE_LOADING = "loading"
        private const val STATE_QUERY = "query"
        private const val STATE_SORT = "sort"
    }

    @Inject lateinit var studentsAdapter: StudentsAdapter

    var query: String = ""
        set(value) {
            field = value
            val actualSort = if (sort.isNotEmpty()) SortType.BY_MARK else sort
            studentsAdapter.filterAndSort(value, actualSort)
            recycler.scrollToPosition(0)
            checkAccountsCount()
        }
    var sort: String = SortType.NONE
        set(value) {
            field = value
            studentsAdapter.filterAndSort(query, value)
            recycler.scrollToPosition(0)
        }

    @Inject lateinit var preferences: SharedPreferences
    @Inject lateinit var studentsPresenter: StudentsPresenter
    override fun createPresenter(): StudentsPresenter = studentsPresenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is InjectorProvider)
            context.inject(this)
        else
            throw IllegalStateException("$context must implement InjectorProvider")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            val wasLoading = savedInstanceState.getBoolean(STATE_LOADING)
            swipeRefresh.isRefreshing = wasLoading
            query = savedInstanceState.getString(STATE_QUERY).orEmpty()
            sort = savedInstanceState.getString(STATE_SORT) ?: SortType.NONE
        }

        recycler.apply {
            adapter = studentsAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(ListItemDecoration(context))
            itemAnimator = PopupItemAnimator()
        }
        swipeRefresh.apply {
            setColorSchemeResources(*swipeRefreshColors)
            setOnRefreshListener { presenter.loadData() }
        }
        presenter.loadData()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(STATE_LOADING, swipeRefresh.isRefreshing)
        outState.putString(STATE_QUERY, query)
        outState.putString(STATE_SORT, sort)
    }

    override fun setLoading(loading: Boolean) {
        swipeRefresh.isRefreshing = loading
    }

    override fun showStudents(students: List<StudentEntity>) {
        studentsAdapter.setNewData(students, query, sort)
        checkAccountsCount()
    }

    override fun showNetworkError() {
        Toast.makeText(context, R.string.network_error, Toast.LENGTH_SHORT).show()
    }

    override fun checkAccountsCount() {
        emptyList.visibility = if (studentsAdapter.itemCount == 0) View.VISIBLE else View.GONE
    }

    override fun openAuthorizationActivity() {
        preferences.edit().clear().apply()
        startActivity(Intent(context, AuthorizationActivity::class.java))
        activity?.finish()
    }
}