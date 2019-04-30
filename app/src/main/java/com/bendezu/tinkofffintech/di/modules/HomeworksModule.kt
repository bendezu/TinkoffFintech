package com.bendezu.tinkofffintech.di.modules

import android.content.SharedPreferences
import com.bendezu.tinkofffintech.courses.rating_details.HomeworksRepository
import com.bendezu.tinkofffintech.courses.rating_details.LecturesAdapter
import com.bendezu.tinkofffintech.courses.rating_details.LecturesPresenter
import com.bendezu.tinkofffintech.courses.rating_details.TasksAdapter
import com.bendezu.tinkofffintech.data.FintechDatabase
import com.bendezu.tinkofffintech.di.ActivityScope
import com.bendezu.tinkofffintech.network.FintechApiService
import dagger.Module
import dagger.Provides

@Module
class HomeworksModule {

    @Provides
    @ActivityScope
    fun provideHomeworksRepository(db: FintechDatabase,
                                   preferences: SharedPreferences,
                                   apiService: FintechApiService) =
        HomeworksRepository(db.lectureDao(), db.taskDao(), preferences, apiService)

    @Provides
    @ActivityScope
    fun provideLecturesPresenter(homeworksRepository: HomeworksRepository) = LecturesPresenter(homeworksRepository)

    @Provides
    @ActivityScope
    fun provideLecturesAdapter() = LecturesAdapter()

    @Provides
    @ActivityScope
    fun provideTasksAdapter() = TasksAdapter()
}