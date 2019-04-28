package com.bendezu.tinkofffintech.di.components

import com.bendezu.tinkofffintech.courses.performance_details.PerformanceDetailActivity
import com.bendezu.tinkofffintech.courses.performance_details.StudentsFragment
import com.bendezu.tinkofffintech.di.ActivityScope
import com.bendezu.tinkofffintech.di.modules.StudentsModule
import dagger.Component

@ActivityScope
@Component(dependencies = [AppComponent::class], modules = [StudentsModule::class])
interface  PerformanceDetailComponent {

    fun inject(performanceDetailActivity: PerformanceDetailActivity)

    fun inject(studentsFragment: StudentsFragment)
}