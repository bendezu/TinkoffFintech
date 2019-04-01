package com.bendezu.tinkofffintech.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface LectureDao {

    @Query("SELECT * FROM lecture ORDER BY id DESC")
    fun getAll(): List<LectureEntity>

    @Insert(onConflict = REPLACE)
    fun insertAll(currencies: List<LectureEntity>)

    @Query("DELETE FROM lecture")
    fun deleteAllLectures()

    @Transaction
    fun updateData(lectures: List<LectureEntity>) {
        deleteAllLectures()
        insertAll(lectures)
    }
}

@Dao
interface TaskDao {

    @Query("SELECT * FROM task WHERE lecture_id = :lectureId ORDER BY id DESC")
    fun getByLecture(lectureId: Long): List<TaskEntity>

    @Insert(onConflict = REPLACE)
    fun insertAll(currencies: List<TaskEntity>)

    @Query("DELETE FROM task")
    fun deleteAllTasks()

    @Transaction
    fun updateData(tasks: List<TaskEntity>) {
        deleteAllTasks()
        insertAll(tasks)
    }
}