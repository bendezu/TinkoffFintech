package com.bendezu.tinkofffintech

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.bendezu.tinkofffintech.network.DelayInterceptor
import com.bendezu.tinkofffintech.network.FintechApiService
import com.jakewharton.threetenabp.AndroidThreeTen
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

fun String.getAvatarColor() = avatarColors[Math.abs(this.hashCode()) % avatarColors.size]

fun String.getInitials() =
    this.split(" ", limit = 2).map{ it.firstOrNull() ?: "" }.joinToString("").toUpperCase()

class App: Application() {

    companion object {
        lateinit var apiService: FintechApiService
        lateinit var preferences: SharedPreferences
    }

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)

        val okHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(DelayInterceptor())
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()

        apiService = Retrofit.Builder()
            .baseUrl(FintechApiService.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create()

        preferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }
}