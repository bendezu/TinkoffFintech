package com.bendezu.tinkofffintech.events

import com.bendezu.tinkofffintech.data.dao.EventDao
import com.bendezu.tinkofffintech.data.entity.EventEntity
import com.bendezu.tinkofffintech.di.ActivityScope
import com.bendezu.tinkofffintech.network.FintechApiService
import com.bendezu.tinkofffintech.network.models.toEntity
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

@ActivityScope
class EventsRepository @Inject constructor(private val eventDao: EventDao, private val apiService: FintechApiService) {

    fun getEvents(): Flowable<List<EventEntity>> {

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
        return Single.concat(eventDao.getAllRx(), networkResponse)
    }
}