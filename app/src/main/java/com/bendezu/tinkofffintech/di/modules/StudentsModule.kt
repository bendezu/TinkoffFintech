package com.bendezu.tinkofffintech.di.modules

import android.content.SharedPreferences
import android.util.Log
import com.bendezu.tinkofffintech.courses.performance_details.AccountsPresenter
import com.bendezu.tinkofffintech.courses.performance_details.StudentsRepository
import com.bendezu.tinkofffintech.data.FintechDatabase
import com.bendezu.tinkofffintech.di.ActivityScope
import com.bendezu.tinkofffintech.network.FintechApiService
import dagger.Module
import dagger.Provides

@Module
class StudentsModule {

    @ActivityScope
    @Provides
    fun provideStudentsRepository(db: FintechDatabase,
                                  preferences: SharedPreferences,
                                  apiService: FintechApiService) =
        StudentsRepository(db.studentDao(), preferences, apiService).apply { Log.d("INJECT", toString()) }

    @ActivityScope
    @Provides
    fun provideAccountsPresenter(studentsRepository: StudentsRepository) = AccountsPresenter(studentsRepository).apply { Log.d("INJECT", toString()) }
}