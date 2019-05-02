package com.bendezu.tinkofffintech.data.dao

import androidx.room.*
import com.bendezu.tinkofffintech.data.entity.EventEntity
import io.reactivex.Single

@Dao
interface EventDao {

    @Query("SELECT * FROM event")
    fun getAllRx(): Single<List<EventEntity>>

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