package com.luisfagundes.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.luisfagundes.data.preferences.UserPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {
    @Provides
    @Singleton
    fun provideUserPreferences(
        @ApplicationContext context: Context
    ) = UserPreferences(context)
}