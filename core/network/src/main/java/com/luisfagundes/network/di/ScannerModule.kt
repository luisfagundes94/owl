package com.luisfagundes.network.di

import com.luisfagundes.network.scanner.BruteForceScannerImpl
import com.luisfagundes.network.scanner.DeviceScanner
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