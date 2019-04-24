package com.bendezu.tinkofffintech.di

import android.content.SharedPreferences
import com.bendezu.tinkofffintech.App
import com.bendezu.tinkofffintech.MainActivity
import com.bendezu.tinkofffintech.auth.AuthorizationActivity
import com.bendezu.tinkofffintech.courses.performance_details.AccountListFragment
import com.bendezu.tinkofffintech.courses.performance_details.PerformanceDetailActivity
import com.bendezu.tinkofffintech.courses.rating_details.LecturesFragment
import com.bendezu.tinkofffintech.courses.rating_details.RatingDetailsActivity
import com.bendezu.tinkofffintech.courses.rating_details.TasksFragment
import com.bendezu.tinkofffintech.data.FintechDatabase
import com.bendezu.tinkofffintech.di.modules.*
import com.bendezu.tinkofffintech.network.FintechApiService
import com.bendezu.tinkofffintech.profile.ProfileFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun preferences(): SharedPreferences
    fun apiService(): FintechApiService
    fun db(): FintechDatabase

    fun inject(app: App)
}

@ActivityScope
@Component(dependencies = [AppComponent::class], modules = [AuthModule::class])
interface AuthorizationActivityComponent {
    fun inject(authorizationActivity: AuthorizationActivity)
}

@ActivityScope
@Component(dependencies = [AppComponent::class], modules = [ProfileModule::class])
interface  MainActivityComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(profileFragment: ProfileFragment)
}

@ActivityScope
@Component(dependencies = [AppComponent::class], modules = [StudentsModule::class])
interface  PerformanceDetailComponent {
    fun inject(performanceDetailActivity: PerformanceDetailActivity)
    fun inject(accountListFragment: AccountListFragment)
}

@ActivityScope
@Component(dependencies = [AppComponent::class], modules = [HomeworksModule::class])
interface  RatingDetailsComponent {
    fun inject(ratingDetailsActivity: RatingDetailsActivity)
    fun inject(lecturesFragment: LecturesFragment)
    fun inject(tasksFragment: TasksFragment)
}