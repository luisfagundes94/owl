package com.luisfagundes.data.di

import com.luisfagundes.data.repository.WifiRepositoryImpl
import com.luisfagundes.domain.repository.WifiRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindWifiRepository(impl: WifiRepositoryImpl): WifiRepository
}
