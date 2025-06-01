package com.luisfagundes.history.data.di

import com.luisfagundes.domain.repository.HistoryRepository
import com.luisfagundes.history.data.repository.HistoryRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindHistoryRepository(
        repository: HistoryRepositoryImpl,
    ): HistoryRepository
}