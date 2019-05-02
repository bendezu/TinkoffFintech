package com.bendezu.tinkofffintech.events

import com.bendezu.tinkofffintech.data.entity.EventEntity
import com.bendezu.tinkofffintech.di.ActivityScope
import com.bendezu.tinkofffintech.network.NetworkException
import com.bendezu.tinkofffintech.network.UnauthorizedException
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import javax.inject.Inject

@ActivityScope
class EventsPresenter @Inject constructor(private val repository: EventsRepository) : MvpBasePresenter<EventsView>(),
    EventsRepository.EventsCallback {

    init {
        repository.callback = this
    }

    fun loadData() {
        repository.getEvents()
    }

    override fun destroy() {
        repository.dispose()
        super.destroy()
    }

    override fun onResult(events: List<EventEntity>, shouldStopLoading: Boolean) {
        ifViewAttached { view ->
            view.showActiveEvents(events.filter { it.isActive })
            view.showArchivedEvents(events.filter { !it.isActive })
            if (shouldStopLoading)
                view.setLoading(false)
            else
                if (events.isEmpty()) view.setLoading(true)
        }
    }

    override fun onError(t: Throwable) {
        ifViewAttached {
            it.setLoading(false)
            when (t) {
                is NetworkException -> it.showNetworkError()
                is UnauthorizedException -> it.openAuthorizationActivity()
            }
        }
    }
}