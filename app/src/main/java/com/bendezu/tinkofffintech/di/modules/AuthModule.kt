package com.bendezu.tinkofffintech.di.modules

import android.content.SharedPreferences
import com.bendezu.tinkofffintech.auth.AuthPresenter
import com.bendezu.tinkofffintech.di.ActivityScope
import com.bendezu.tinkofffintech.network.FintechApiService
import dagger.Module
import dagger.Provides

@Module
class AuthModule {

    @Provides
    @ActivityScope
    fun provideAuthPresenter(preferences: SharedPreferences, apiService: FintechApiService) =
        AuthPresenter(preferences, apiService)
}