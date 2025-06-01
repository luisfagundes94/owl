package com.luisfagundes.history.data.di

import com.luisfagundes.common.mapper.Mapper
import com.luisfagundes.domain.model.Device
import com.luisfagundes.history.data.mapper.DeviceEntityMapper
import com.luisfagundes.history.data.mapper.DeviceMapper
import com.luisfagundes.history.data.model.DeviceEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MapperModule {
    @Provides
    fun provideDeviceEntityMapper(): Mapper<Device, DeviceEntity> {
        return DeviceEntityMapper()
    }

    @Provides
    fun provideDeviceMapper(): Mapper<DeviceEntity, Device> {
        return DeviceMapper()
    }
}