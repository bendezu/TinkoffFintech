package com.bendezu.tinkofffintech

import android.app.Application
import com.bendezu.tinkofffintech.di.components.AppComponent
import com.bendezu.tinkofffintech.di.components.DaggerAppComponent
import com.bendezu.tinkofffintech.di.modules.AppModule
import com.jakewharton.threetenabp.AndroidThreeTen

fun String.getAvatarColor() = avatarColors[Math.abs(this.hashCode()) % avatarColors.size]

fun String.getInitials() =
    this.split(" ", limit = 2).map{ it.firstOrNull() ?: "" }.joinToString("").toUpperCase()

class App: Application() {

    companion object {
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        component = DaggerAppComponent.builder()
            .appModule(AppModule(applicationContext))
            .build()

        AndroidThreeTen.init(this)
    }
}