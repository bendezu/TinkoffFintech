package com.bendezu.tinkofffintech.di.components

import com.bendezu.tinkofffintech.auth.AuthorizationActivity
import com.bendezu.tinkofffintech.di.ActivityScope
import com.bendezu.tinkofffintech.di.modules.AuthModule
import dagger.Component

@ActivityScope
@Component(dependencies = [AppComponent::class], modules = [AuthModule::class])
interface AuthorizationActivityComponent {

    fun inject(authorizationActivity: AuthorizationActivity)
}