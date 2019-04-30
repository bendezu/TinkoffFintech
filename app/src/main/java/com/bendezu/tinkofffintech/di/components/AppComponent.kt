package com.bendezu.tinkofffintech.di.components

import android.content.Context
import android.content.SharedPreferences
import com.bendezu.tinkofffintech.App
import com.bendezu.tinkofffintech.data.FintechDatabase
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

    fun inject(app: App)
}