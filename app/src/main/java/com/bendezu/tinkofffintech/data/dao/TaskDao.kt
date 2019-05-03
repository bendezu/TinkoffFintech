package com.bendezu.tinkofffintech.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bendezu.tinkofffintech.data.entity.TaskEntity
import io.reactivex.Single

@Dao
interface TaskDao {

    @Query("SELECT * FROM task WHERE lecture_id = :lectureId ORDER BY id DESC")
    fun getByLecture(lectureId: Long): List<TaskEntity>

    @Query("SELECT * FROM task")
    fun getAllRx(): Single<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(tasks: List<TaskEntity>)

    @Query("DELETE FROM task")
    fun deleteAll()
}