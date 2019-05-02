package com.bendezu.tinkofffintech.events

import com.bendezu.tinkofffintech.data.dao.EventDao
import com.bendezu.tinkofffintech.data.entity.EventEntity
import com.bendezu.tinkofffintech.di.ActivityScope
import com.bendezu.tinkofffintech.network.FintechApiService
import com.bendezu.tinkofffintech.network.NetworkException
import com.bendezu.tinkofffintech.network.UnauthorizedException
import com.bendezu.tinkofffintech.network.models.Event
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

@ActivityScope
class EventsRepository @Inject constructor(private val eventDao: EventDao, private val apiService: FintechApiService) {

    var callback: EventsCallback? = null

    interface EventsCallback {
        fun onResult(events: List<EventEntity>, shouldStopLoading: Boolean = false)
        fun onError(t: Throwable)
    }

    private val disposables = CompositeDisposable()

    fun getEvents() {
        var shouldStopLoading = false
        val networkResponse = apiService.getEventsRx()
            .flatMap { events ->
                val result = mutableListOf<EventEntity>()
                for (event in events.activeEvents)
                    result.add(event.toEntity(true))
                for (event in events.archivedEvents)
                    result.add(event.toEntity(false))
                eventDao.updateData(result)
                return@flatMap Single.fromCallable { result }
            }
        disposables += Single.concat(eventDao.getAllRx(), networkResponse)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread(), true)
            .subscribe ({ events ->
                callback?.onResult(events, shouldStopLoading)
                shouldStopLoading = true
            }, { t ->
                when (t) {
                    is HttpException -> if (t.code() == 403) callback?.onError(UnauthorizedException())
                    else -> callback?.onError(NetworkException())
                }
            })
    }

    fun dispose() {
        disposables.clear()
    }

    private fun Event.toEntity(isActive: Boolean) = EventEntity (
        isActive, title, startDate, endDate, customDate, place,
        url, isUrlExternal, shouldDisplayButton, urlText, description, eventType?.name, eventType?.color
    )
}