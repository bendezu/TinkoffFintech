package com.bendezu.tinkofffintech.events

import com.bendezu.tinkofffintech.data.entity.EventEntity
import com.bendezu.tinkofffintech.di.ActivityScope
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

@ActivityScope
class EventsPresenter @Inject constructor(private val repository: EventsRepository) : MvpBasePresenter<EventsView>() {

    private val disposables = CompositeDisposable()

    fun loadData() {
        disposables += repository.getEvents()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread(), true)
            .subscribe(::onResult, ::onError) { ifViewAttached { it.setLoading(false) } }
    }

    override fun destroy() {
        disposables.clear()
        super.destroy()
    }

    private fun onResult(events: List<EventEntity>) {
        ifViewAttached { view ->
            view.showActiveEvents(events.filter { it.isActive })
            view.showArchivedEvents(events.filter { !it.isActive })
            if (events.isEmpty()) view.setLoading(true)
        }
    }

    private fun onError(t: Throwable) {
        ifViewAttached {
            it.setLoading(false)
            when (t) {
                is HttpException -> if (t.code() == 403) it.openAuthorizationActivity()
                else -> it.showNetworkError()
            }
        }
    }
}