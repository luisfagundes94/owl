package com.luisfagundes.history.data.di

import com.luisfagundes.history.data.mapper.DeviceEntityMapper
import com.luisfagundes.history.data.mapper.DeviceMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal class MapperModule {
    @Provides
    fun provideDeviceEntityMapper() = DeviceEntityMapper()

    @Provides
    fun provideDeviceMapper() = DeviceMapper()
}
