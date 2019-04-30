package com.bendezu.tinkofffintech.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.bendezu.tinkofffintech.courses.performance_details.StudentsAdapter
import com.bendezu.tinkofffintech.courses.performance_details.StudentsPresenter
import com.bendezu.tinkofffintech.courses.performance_details.StudentsRepository
import com.bendezu.tinkofffintech.data.FintechDatabase
import com.bendezu.tinkofffintech.di.ActivityScope
import com.bendezu.tinkofffintech.network.FintechApiService
import dagger.Module
import dagger.Provides

@Module
class StudentsModule {

    @Provides
    @ActivityScope
    fun provideStudentsRepository(db: FintechDatabase,
                                  preferences: SharedPreferences,
                                  apiService: FintechApiService,
                                  context: Context) =
        StudentsRepository(db.studentDao(), preferences, apiService, context)

    @Provides
    @ActivityScope
    fun provideAccountsPresenter(studentsRepository: StudentsRepository) = StudentsPresenter(studentsRepository)

    @Provides
    @ActivityScope
    fun provideAccountsAdapter() = StudentsAdapter()
}