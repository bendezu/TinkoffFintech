package com.bendezu.tinkofffintech

import android.app.Application
import com.bendezu.tinkofffintech.di.Injector
import com.jakewharton.threetenabp.AndroidThreeTen

fun String.getAvatarColor() = avatarColors[Math.abs(this.hashCode()) % avatarColors.size]

fun String.getInitials() =
    this.split(" ", limit = 2).map{ it.firstOrNull() ?: "" }.joinToString("").toUpperCase()

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        Injector.init(this)
        AndroidThreeTen.init(this)
    }
}