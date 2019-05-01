package com.bendezu.tinkofffintech.di.component

import android.content.Context
import android.content.SharedPreferences
import com.bendezu.tinkofffintech.App
import com.bendezu.tinkofffintech.data.FintechDatabase
import com.bendezu.tinkofffintech.data.dao.EventDao
import com.bendezu.tinkofffintech.data.dao.LectureDao
import com.bendezu.tinkofffintech.data.dao.StudentDao
import com.bendezu.tinkofffintech.data.dao.TaskDao
import com.bendezu.tinkofffintech.di.module.AppModule
import com.bendezu.tinkofffintech.network.FintechApiService
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun context(): Context

    fun preferences(): SharedPreferences

    fun apiService(): FintechApiService

    fun db(): FintechDatabase

    fun lectureDao(): LectureDao

    fun taskDao(): TaskDao

    fun studentDao(): StudentDao

    fun eventDao(): EventDao

    fun inject(app: App)
}