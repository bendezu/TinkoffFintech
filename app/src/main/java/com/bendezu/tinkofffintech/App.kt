package com.bendezu.tinkofffintech

import android.app.Application
import com.bendezu.tinkofffintech.network.DelayInterceptor
import com.bendezu.tinkofffintech.network.FintechApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


class App: Application() {

    companion object {
        lateinit var apiService: FintechApiService
    }

    override fun onCreate() {
        super.onCreate()

        val okHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(DelayInterceptor())
            .build()

        apiService = Retrofit.Builder()
            .baseUrl(FintechApiService.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create()
    }
}