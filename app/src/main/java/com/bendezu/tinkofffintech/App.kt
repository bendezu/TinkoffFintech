package com.bendezu.tinkofffintech

import android.app.Application
import com.bendezu.tinkofffintech.network.FintechApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class App: Application() {

    companion object {
        lateinit var apiService: FintechApiService
    }

    override fun onCreate() {
        super.onCreate()

        apiService = Retrofit.Builder()
            .baseUrl(FintechApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create()
    }
}