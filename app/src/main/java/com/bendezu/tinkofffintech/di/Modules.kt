package com.bendezu.tinkofffintech.di

import android.content.Context
import android.content.SharedPreferences
import com.bendezu.tinkofffintech.SHARED_PREFERENCES_NAME
import com.bendezu.tinkofffintech.auth.AuthPresenter
import com.bendezu.tinkofffintech.courses.performance_details.AccountsPresenter
import com.bendezu.tinkofffintech.courses.performance_details.StudentsRepository
import com.bendezu.tinkofffintech.courses.rating_details.HomeworksRepository
import com.bendezu.tinkofffintech.courses.rating_details.LecturesPresenter
import com.bendezu.tinkofffintech.data.FintechDatabase
import com.bendezu.tinkofffintech.network.DelayInterceptor
import com.bendezu.tinkofffintech.network.FintechApiService
import com.bendezu.tinkofffintech.profile.ProfilePresenter
import com.bendezu.tinkofffintech.profile.ProfileRepository
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
    fun provideContext() = context

    @Singleton @Provides
    fun provideDatabase(context: Context) = FintechDatabase.getInstance(context)

    @Singleton @Provides
    fun providePreferences(context: Context) = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    @Singleton @Provides
    fun provideOkHttpClient() = OkHttpClient.Builder()
        .addNetworkInterceptor(DelayInterceptor())
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()

    @Singleton @Provides
    fun provideApiService(okHttpClient: OkHttpClient) = Retrofit.Builder()
        .baseUrl(FintechApiService.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create<FintechApiService>()
}

@Module
class AuthModule {
    @Singleton @Provides
    fun provideAuthPresenter(preferences: SharedPreferences, apiService: FintechApiService) =
        AuthPresenter(preferences, apiService)
}

@Module
class ProfileModule {
    @Singleton @Provides
    fun provideProfileRepository(preferences: SharedPreferences, apiService: FintechApiService) =
        ProfileRepository(preferences, apiService)

    @Singleton @Provides
    fun provideProfilePresenter(profileRepository: ProfileRepository) = ProfilePresenter(profileRepository)
}

@Module
class StudentsModule {
    @Singleton @Provides
    fun provideStudentsRepository(db: FintechDatabase,
                                  preferences: SharedPreferences,
                                  apiService: FintechApiService)  =
        StudentsRepository(db.studentDao(), preferences, apiService)

    @Singleton @Provides
    fun provideAccountsPresenter(studentsRepository: StudentsRepository) = AccountsPresenter(studentsRepository)
}

@Module
class HomeworksModule {
    @Singleton @Provides
    fun provideHomeworksRepository(db: FintechDatabase,
                                   preferences: SharedPreferences,
                                   apiService: FintechApiService) =
        HomeworksRepository(db.lectureDao(), db.taskDao(), preferences, apiService)

    @Singleton @Provides
    fun provideLecturesPresenter(homeworksRepository: HomeworksRepository) = LecturesPresenter(homeworksRepository)
}