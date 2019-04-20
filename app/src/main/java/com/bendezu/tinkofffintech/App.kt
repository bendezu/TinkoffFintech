package com.bendezu.tinkofffintech

import android.app.Application
import com.bendezu.tinkofffintech.di.AppComponent
import com.bendezu.tinkofffintech.di.AppModule
import com.bendezu.tinkofffintech.di.DaggerAppComponent
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