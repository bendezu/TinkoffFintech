package com.bendezu.tinkofffintech.data.dao

import androidx.room.*
import com.bendezu.tinkofffintech.data.entity.TaskEntity

@Dao
interface TaskDao {

    @Query("SELECT * FROM task WHERE lecture_id = :lectureId ORDER BY id DESC")
    fun getByLecture(lectureId: Long): List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(tasks: List<TaskEntity>)

    @Query("DELETE FROM task")
    fun deleteAllTasks()

    @Transaction
    fun updateData(tasks: List<TaskEntity>) {
        deleteAllTasks()
        insertAll(tasks)
    }
}