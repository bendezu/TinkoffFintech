package com.bendezu.tinkofffintech.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bendezu.tinkofffintech.data.dao.LectureDao
import com.bendezu.tinkofffintech.data.dao.StudentDao
import com.bendezu.tinkofffintech.data.dao.TaskDao
import com.bendezu.tinkofffintech.data.entity.LectureEntity
import com.bendezu.tinkofffintech.data.entity.StudentEntity
import com.bendezu.tinkofffintech.data.entity.TaskEntity

@Database(entities = [LectureEntity::class, TaskEntity::class, StudentEntity::class], version = 2)
abstract class FintechDatabase: RoomDatabase() {

    abstract fun lectureDao(): LectureDao
    abstract fun taskDao(): TaskDao
    abstract fun studentDao(): StudentDao

    companion object {
        private var INSTANCE: FintechDatabase? = null

        fun getInstance(context: Context): FintechDatabase {
            if (INSTANCE == null) {
                synchronized(Database::class) {
                    if (INSTANCE == null)
                        INSTANCE = buildDatabase(context)
                }
            }
            return INSTANCE!!
        }

        private fun buildDatabase(context: Context) : FintechDatabase =
            Room.databaseBuilder(context.applicationContext,
                FintechDatabase::class.java, "fintech.db")
                .fallbackToDestructiveMigration()
                .build()

        fun destroyInstance() {
            INSTANCE = null
        }
    }

}