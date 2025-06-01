package com.luisfagundes.network.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import com.luisfagundes.network.monitor.NetworkMonitor
import com.luisfagundes.network.monitor.NetworkMonitorImpl
import com.luisfagundes.network.wifi.AppWifiManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConnectivityModule {
    @Provides
    @Singleton
    fun provideConnectivityManager(
        @ApplicationContext appContext: Context
    ): ConnectivityManager =
        appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    fun provideWifiManager(
        @ApplicationContext appContext: Context
    ): WifiManager =
        appContext.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    @Provides
    @Singleton
    fun provideOwlWifiManager(
        wifiManager: WifiManager
    ) = AppWifiManager(wifiManager)

    @Provides
    @Singleton
    fun provideNetworkMonitor(
        @ApplicationContext appContext: Context
    ): NetworkMonitor = NetworkMonitorImpl(appContext)
}