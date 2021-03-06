package com.bendezu.tinkofffintech.di.component

import com.bendezu.tinkofffintech.auth.AuthorizationActivity
import com.bendezu.tinkofffintech.di.ActivityScope
import dagger.Component

@ActivityScope
@Component(dependencies = [AppComponent::class])
interface AuthorizationActivityComponent {

    fun inject(authorizationActivity: AuthorizationActivity)
}