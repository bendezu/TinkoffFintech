package com.bendezu.tinkofffintech.di

import android.content.Context
import com.bendezu.tinkofffintech.di.component.*
import com.bendezu.tinkofffintech.di.module.AppModule

object Injector {

    lateinit var appComponent: AppComponent

    fun init(context: Context) {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(context))
            .build()
    }

    fun authActivityComponent() = DaggerAuthorizationActivityComponent.builder().appComponent(appComponent).build()

    fun mainActivityComponent() = DaggerMainActivityComponent.builder().appComponent(appComponent).build()

    fun performanceDetailComponent() = DaggerPerformanceDetailComponent.builder().appComponent(appComponent).build()

    fun ratingDetailsComponent() = DaggerRatingDetailsComponent.builder().appComponent(appComponent).build()
}