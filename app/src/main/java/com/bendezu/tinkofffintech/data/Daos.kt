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
    fun insertAll(lectures: List<LectureEntity>)

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
    fun insertAll(tasks: List<TaskEntity>)

    @Query("DELETE FROM task")
    fun deleteAllTasks()

    @Transaction
    fun updateData(tasks: List<TaskEntity>) {
        deleteAllTasks()
        insertAll(tasks)
    }
}

@Dao
interface StudentDao {

    @Query("SELECT * FROM student ORDER BY id ASC")
    fun getAll(): List<StudentEntity>

    @Insert(onConflict = REPLACE)
    fun insertAll(students: List<StudentEntity>)

    @Query("DELETE FROM student")
    fun deleteAll()

    @Transaction
    fun updateData(students: List<StudentEntity>) {
        deleteAll()
        insertAll(students)
    }
}