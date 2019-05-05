package com.bendezu.tinkofffintech.di.module

import android.content.Context
import com.bendezu.tinkofffintech.SHARED_PREFERENCES_NAME
import com.bendezu.tinkofffintech.data.FintechDatabase
import com.bendezu.tinkofffintech.network.FintechApiService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
class AppModule(val context: Context) {

    @Provides
    @Singleton
    fun provideContext() = context

    @Provides
    @Singleton
    fun provideDatabase(context: Context) = FintechDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideLectureDao(db: FintechDatabase) = db.lectureDao()

    @Provides
    @Singleton
    fun provideTaskDao(db: FintechDatabase) = db.taskDao()

    @Provides
    @Singleton
    fun provideStudentDao(db: FintechDatabase) = db.studentDao()

    @Provides
    @Singleton
    fun provideEventDao(db: FintechDatabase) = db.eventDao()

    @Provides
    @Singleton
    fun providePreferences(context: Context) = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideOkHttpClient() = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()

    @Provides
    @Singleton
    fun provideApiService(okHttpClient: OkHttpClient) = Retrofit.Builder()
        .baseUrl(FintechApiService.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build().create<FintechApiService>()
}