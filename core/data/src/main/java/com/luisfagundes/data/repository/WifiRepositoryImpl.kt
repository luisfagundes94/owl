package com.luisfagundes.data.repository

import com.luisfagundes.domain.repository.WifiRepository
import com.luisfagundes.network.wifi.AppWifiManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WifiRepositoryImpl @Inject constructor(
    private val wifiManager: AppWifiManager
): WifiRepository {
    override fun getSsid(): Flow<String?> {
        return wifiManager.ssidName
    }
}