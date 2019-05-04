package com.bendezu.tinkofffintech.di.component

import com.bendezu.tinkofffintech.courses.course_details.CourseDetailActivity
import com.bendezu.tinkofffintech.di.ActivityScope
import dagger.Component

@ActivityScope
@Component(dependencies = [AppComponent::class])
interface  CourseDetailComponent {

    fun inject(courseDetailActivity: CourseDetailActivity)

}