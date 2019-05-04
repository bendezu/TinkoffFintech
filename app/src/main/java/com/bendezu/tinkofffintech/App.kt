package com.bendezu.tinkofffintech

import android.app.Application
import android.content.Context
import androidx.core.content.ContextCompat
import com.bendezu.tinkofffintech.di.Injector
import com.jakewharton.threetenabp.AndroidThreeTen

fun String.getAvatarColor() = avatarColors[Math.abs(this.hashCode()) % avatarColors.size]

fun String.getInitials() =
    this.split(" ", limit = 2).map{ it.firstOrNull() ?: "" }.joinToString("").toUpperCase()

fun String?.parseColor(context: Context) = when (this) {
    "purple" -> ContextCompat.getColor(context, R.color.purple)
    "orange" -> ContextCompat.getColor(context, R.color.orange)
    "green" -> ContextCompat.getColor(context, R.color.green)
    else -> ContextCompat.getColor(context, R.color.blue)
}

fun String?.getColoredEventIcon(context: Context) = when (this) {
    "purple" -> context.getDrawable(R.drawable.ic_event_purple)
    "orange" -> context.getDrawable(R.drawable.ic_event_orange)
    "green" -> context.getDrawable(R.drawable.ic_event_green)
    else -> context.getDrawable(R.drawable.ic_event_blue)
}

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        Injector.init(this)
        AndroidThreeTen.init(this)
    }
}