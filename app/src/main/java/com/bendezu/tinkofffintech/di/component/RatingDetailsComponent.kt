package com.bendezu.tinkofffintech.di.component

import com.bendezu.tinkofffintech.courses.rating_details.LecturesFragment
import com.bendezu.tinkofffintech.courses.rating_details.RatingDetailsActivity
import com.bendezu.tinkofffintech.courses.rating_details.TasksFragment
import com.bendezu.tinkofffintech.di.ActivityScope
import dagger.Component

@ActivityScope
@Component(dependencies = [AppComponent::class])
interface  RatingDetailsComponent {

    fun inject(ratingDetailsActivity: RatingDetailsActivity)

    fun inject(lecturesFragment: LecturesFragment)

    fun inject(tasksFragment: TasksFragment)
}