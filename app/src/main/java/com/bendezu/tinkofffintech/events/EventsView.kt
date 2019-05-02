package com.bendezu.tinkofffintech.events

import com.bendezu.tinkofffintech.data.entity.EventEntity
import com.hannesdorfmann.mosby3.mvp.MvpView

interface EventsView: MvpView {
    fun showActiveEvents(events: List<EventEntity>)
    fun showArchivedEvents(events: List<EventEntity>)
    fun showNetworkError()
    fun openAuthorizationActivity()
    fun setLoading(loading: Boolean)
}