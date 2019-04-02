package com.bendezu.tinkofffintech.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [(LectureEntity::class), (TaskEntity::class)], version = 1)
abstract class FintechDatabase: RoomDatabase() {

    abstract fun lectureDao(): LectureDao
    abstract fun taskDao(): TaskDao

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
                .build()

        fun destroyInstance() {
            INSTANCE = null
        }
    }

}