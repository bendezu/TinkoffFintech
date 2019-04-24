package com.bendezu.tinkofffintech.di

import android.content.Context
import com.bendezu.tinkofffintech.di.components.*
import com.bendezu.tinkofffintech.di.modules.AppModule

object Injector {

    lateinit var appComponent: AppComponent

    fun init(context: Context) {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(context))
            .build()
    }

    fun authActivityComponent() = DaggerAuthorizationActivityComponent.builder().appComponent(appComponent).build()

    fun profileFragmentComponent() = DaggerProfileFragmentComponent.builder().appComponent(appComponent).build()

    fun accountListFragmentComponent() = DaggerAccountListFragmentComponent.builder().appComponent(appComponent).build()

    fun lecturesFragmentComponent() = DaggerLecturesFragmentComponent.builder().appComponent(appComponent).build()
}