package com.luisfagundes.device.data.di

import com.luisfagundes.device.data.scanner.BruteForceDeviceScannerImpl
import com.luisfagundes.device.data.scanner.DeviceScanner
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ScannerModule {
    @Binds
    @Singleton
    abstract fun bindDeviceScanner(impl: BruteForceDeviceScannerImpl): DeviceScanner
}
