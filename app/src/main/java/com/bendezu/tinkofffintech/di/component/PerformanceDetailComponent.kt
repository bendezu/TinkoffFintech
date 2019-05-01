package com.bendezu.tinkofffintech.di.component

import com.bendezu.tinkofffintech.courses.performance_details.PerformanceDetailActivity
import com.bendezu.tinkofffintech.courses.performance_details.StudentsFragment
import com.bendezu.tinkofffintech.di.ActivityScope
import dagger.Component

@ActivityScope
@Component(dependencies = [AppComponent::class])
interface  PerformanceDetailComponent {

    fun inject(performanceDetailActivity: PerformanceDetailActivity)

    fun inject(studentsFragment: StudentsFragment)
}