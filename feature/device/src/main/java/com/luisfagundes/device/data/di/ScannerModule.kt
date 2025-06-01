package com.luisfagundes.device.data.di

import com.luisfagundes.device.data.scanner.BruteForceScannerImpl
import com.luisfagundes.device.data.scanner.DeviceScanner
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ScannerModule {
    @Binds
    @Singleton
    abstract fun bindDeviceScanner(
        impl: BruteForceScannerImpl
    ): DeviceScanner
}