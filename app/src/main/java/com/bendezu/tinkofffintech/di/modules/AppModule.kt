package com.bendezu.tinkofffintech.di.modules

import android.content.Context
import android.util.Log
import com.bendezu.tinkofffintech.SHARED_PREFERENCES_NAME
import com.bendezu.tinkofffintech.data.FintechDatabase
import com.bendezu.tinkofffintech.network.DelayInterceptor
import com.bendezu.tinkofffintech.network.FintechApiService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
class AppModule(val context: Context) {

    @Singleton @Provides
    fun provideContext() = context.apply { Log.d("INJECT", toString()) }

    @Singleton @Provides
    fun provideDatabase(context: Context) = FintechDatabase.getInstance(context).apply { Log.d("INJECT", toString()) }

    @Singleton @Provides
    fun providePreferences(context: Context) = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).apply { Log.d("INJECT", toString()) }

    @Singleton @Provides
    fun provideOkHttpClient() = OkHttpClient.Builder()
        .addNetworkInterceptor(DelayInterceptor())
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build().apply { Log.d("INJECT", toString()) }

    @Singleton @Provides
    fun provideApiService(okHttpClient: OkHttpClient) = Retrofit.Builder()
        .baseUrl(FintechApiService.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create<FintechApiService>().apply { Log.d("INJECT", toString()) }
}