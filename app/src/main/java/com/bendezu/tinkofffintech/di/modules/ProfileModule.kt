package com.bendezu.tinkofffintech.di.modules

import android.content.SharedPreferences
import com.bendezu.tinkofffintech.di.ActivityScope
import com.bendezu.tinkofffintech.network.FintechApiService
import com.bendezu.tinkofffintech.profile.ProfilePresenter
import com.bendezu.tinkofffintech.profile.ProfileRepository
import dagger.Module
import dagger.Provides

@Module
class ProfileModule {

    @Provides
    @ActivityScope
    fun provideProfileRepository(preferences: SharedPreferences, apiService: FintechApiService) =
        ProfileRepository(preferences, apiService)

    @Provides
    @ActivityScope
    fun provideProfilePresenter(profileRepository: ProfileRepository) = ProfilePresenter(profileRepository)
}