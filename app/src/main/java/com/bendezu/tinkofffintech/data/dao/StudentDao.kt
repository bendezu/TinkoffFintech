package com.bendezu.tinkofffintech.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.bendezu.tinkofffintech.data.entity.StudentEntity
import io.reactivex.Single

@Dao
interface StudentDao {

    @Query("SELECT * FROM student ORDER BY id ASC")
    fun getAll(): List<StudentEntity>

    @Query("SELECT * FROM student ORDER BY id ASC")
    fun getAllRx(): Single<List<StudentEntity>>

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