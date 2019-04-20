package com.bendezu.tinkofffintech

import android.app.Application
import android.content.Context
import com.bendezu.tinkofffintech.di.AppComponent
import com.bendezu.tinkofffintech.di.DaggerAppComponent
import com.jakewharton.threetenabp.AndroidThreeTen

fun String.getAvatarColor() = avatarColors[Math.abs(this.hashCode()) % avatarColors.size]

fun String.getInitials() =
    this.split(" ", limit = 2).map{ it.firstOrNull() ?: "" }.joinToString("").toUpperCase()

class App: Application() {

    companion object {
        lateinit var context: Context
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        context = applicationContext

        component = DaggerAppComponent.create()

        AndroidThreeTen.init(this)
    }
}