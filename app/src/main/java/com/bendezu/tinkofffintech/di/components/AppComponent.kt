package com.bendezu.tinkofffintech.di.components

import android.content.Context
import android.content.SharedPreferences
import com.bendezu.tinkofffintech.App
import com.bendezu.tinkofffintech.data.FintechDatabase
import com.bendezu.tinkofffintech.data.LectureDao
import com.bendezu.tinkofffintech.data.StudentDao
import com.bendezu.tinkofffintech.data.TaskDao
import com.bendezu.tinkofffintech.di.modules.AppModule
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

    fun inject(app: App)
}