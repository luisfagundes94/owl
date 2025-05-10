package com.luisfagundes.device.data.di

import com.luisfagundes.device.data.repository.DeviceRepositoryImpl
import com.luisfagundes.device.domain.repository.DeviceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindDeviceRepository(
        impl: DeviceRepositoryImpl
    ): DeviceRepository
}