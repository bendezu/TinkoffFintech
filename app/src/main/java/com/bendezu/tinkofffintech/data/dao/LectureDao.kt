package com.bendezu.tinkofffintech.data.dao

import androidx.room.*
import com.bendezu.tinkofffintech.data.entity.LectureEntity
import io.reactivex.Single

@Dao
interface LectureDao {

    @Query("SELECT * FROM lecture ORDER BY id DESC")
    fun getAll(): List<LectureEntity>

    @Query("SELECT * FROM lecture ORDER BY id DESC")
    fun getAllRx(): Single<List<LectureEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(lectures: List<LectureEntity>)

    @Query("DELETE FROM lecture")
    fun deleteAllLectures()

    @Transaction
    fun updateData(lectures: List<LectureEntity>) {
        deleteAllLectures()
        insertAll(lectures)
    }
}