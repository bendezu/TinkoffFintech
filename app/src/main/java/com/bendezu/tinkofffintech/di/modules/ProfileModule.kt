package com.bendezu.tinkofffintech.di.modules

import android.content.SharedPreferences
import android.util.Log
import com.bendezu.tinkofffintech.di.ActivityScope
import com.bendezu.tinkofffintech.network.FintechApiService
import com.bendezu.tinkofffintech.profile.ProfilePresenter
import com.bendezu.tinkofffintech.profile.ProfileRepository
import dagger.Module
import dagger.Provides

@Module
class ProfileModule {

    @ActivityScope
    @Provides
    fun provideProfileRepository(preferences: SharedPreferences, apiService: FintechApiService) =
        ProfileRepository(preferences, apiService).apply { Log.d("INJECT", toString()) }

    @ActivityScope
    @Provides
    fun provideProfilePresenter(profileRepository: ProfileRepository) = ProfilePresenter(profileRepository).apply { Log.d("INJECT", toString()) }
}