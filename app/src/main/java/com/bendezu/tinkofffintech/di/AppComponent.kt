package com.bendezu.tinkofffintech.di

import com.bendezu.tinkofffintech.App
import com.bendezu.tinkofffintech.auth.AuthorizationActivity
import com.bendezu.tinkofffintech.courses.performance_details.AccountListFragment
import com.bendezu.tinkofffintech.courses.rating_details.LecturesFragment
import com.bendezu.tinkofffintech.profile.ProfileFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(app: App)
    fun inject(authorizationActivity: AuthorizationActivity)
    fun inject(profileFragment: ProfileFragment)
    fun inject(accountListFragment: AccountListFragment)
    fun inject(lecturesFragment: LecturesFragment)
}