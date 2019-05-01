package com.bendezu.tinkofffintech.data.dao

import androidx.room.*
import com.bendezu.tinkofffintech.data.entity.EventEntity
import io.reactivex.Single

@Dao
interface EventDao {

    @Query("SELECT * FROM event WHERE is_active = 1")
    fun getAllActiveRx(): Single<List<EventEntity>>

    @Query("SELECT * FROM event WHERE is_active = 0")
    fun getAllArchivedRx(): Single<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(events: List<EventEntity>)

    @Query("DELETE FROM event")
    fun deleteAll()

    @Transaction
    fun updateData(events: List<EventEntity>) {
        deleteAll()
        insertAll(events)
    }

}