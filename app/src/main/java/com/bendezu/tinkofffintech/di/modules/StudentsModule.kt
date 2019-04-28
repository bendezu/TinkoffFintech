package com.bendezu.tinkofffintech.di.modules

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

    @ActivityScope
    @Provides
    fun provideStudentsRepository(db: FintechDatabase,
                                  preferences: SharedPreferences,
                                  apiService: FintechApiService) =
        StudentsRepository(db.studentDao(), preferences, apiService)

    @ActivityScope
    @Provides
    fun provideAccountsPresenter(studentsRepository: StudentsRepository) = StudentsPresenter(studentsRepository)

    @ActivityScope
    @Provides
    fun provideAccountsAdapter() = StudentsAdapter()
}