package com.bendezu.tinkofffintech.di.component

import com.bendezu.tinkofffintech.MainActivity
import com.bendezu.tinkofffintech.courses.CoursesFragment
import com.bendezu.tinkofffintech.di.ActivityScope
import com.bendezu.tinkofffintech.events.EventsFragment
import com.bendezu.tinkofffintech.profile.ProfileFragment
import dagger.Component

@ActivityScope
@Component(dependencies = [AppComponent::class])
interface  MainActivityComponent {

    fun inject(mainActivity: MainActivity)

    fun inject(profileFragment: ProfileFragment)

    fun inject(eventsFragment: EventsFragment)

    fun inject(coursesFragment: CoursesFragment)
}