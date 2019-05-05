package com.bendezu.tinkofffintech.events

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bendezu.tinkofffintech.R
import com.bendezu.tinkofffintech.auth.AuthorizationActivity
import com.bendezu.tinkofffintech.courses.performance_details.ListItemDecoration
import com.bendezu.tinkofffintech.data.entity.EventEntity
import com.bendezu.tinkofffintech.swipeRefreshColors
import com.hannesdorfmann.mosby3.mvp.MvpFragment
import kotlinx.android.synthetic.main.fragment_events.*
import javax.inject.Inject

class EventsFragment: MvpFragment<EventsView, EventsPresenter>(), EventsView {

    interface InjectorProvider {
        fun inject(eventsFragment: EventsFragment)
    }

    companion object {
        private const val STATE_LOADING = "loading"
    }

    @Inject lateinit var activeEventsAdapter: ActiveEventsAdapter
    @Inject lateinit var archivedEventsAdapter: ArchivedEventsAdapter
    @Inject lateinit var preferences: SharedPreferences
    @Inject lateinit var eventsPresenter: EventsPresenter
    override fun createPresenter() = eventsPresenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is InjectorProvider)
            context.inject(this)
        else
            throw IllegalArgumentException("$context must implement InjectorProvider")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.requestApplyInsets()
        view.setOnApplyWindowInsetsListener { _, insets ->
            statusBarBackground.layoutParams.height = insets.systemWindowInsetTop
            insets.consumeSystemWindowInsets()
        }

        if (savedInstanceState != null) {
            val wasLoading = savedInstanceState.getBoolean(STATE_LOADING)
            swipeRefresh.isRefreshing = wasLoading
        }

        activeEventsAdapter.listener = this@EventsFragment::onEventClicked
        archivedEventsAdapter.listener = this@EventsFragment::onEventClicked

        activeEventsRecycler.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = activeEventsAdapter
        }
        archivedEventsRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = archivedEventsAdapter
            isNestedScrollingEnabled = false
            addItemDecoration(ListItemDecoration(requireContext()))
        }
        swipeRefresh.apply {
            setColorSchemeResources(*swipeRefreshColors)
            setOnRefreshListener{ presenter.loadData() }
        }
        presenter.loadData()
    }

    private fun onEventClicked(event: EventEntity) {
        val bottomSheet = EventDetailFragment.newInstance(event)
        bottomSheet.show(fragmentManager, bottomSheet.tag)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(STATE_LOADING, swipeRefresh.isRefreshing)
        super.onSaveInstanceState(outState)
    }

    override fun setLoading(loading: Boolean) {
        swipeRefresh.isRefreshing = loading
    }

    override fun showActiveEvents(events: List<EventEntity>) {
        activeEventsAdapter.data = events
        emptyActiveList.visibility = if (activeEventsAdapter.itemCount == 0) View.VISIBLE else View.GONE
    }

    override fun showArchivedEvents(events: List<EventEntity>) {
        archivedEventsAdapter.data = events
        emptyArchivedList.visibility = if (archivedEventsAdapter.itemCount == 0) View.VISIBLE else View.GONE
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