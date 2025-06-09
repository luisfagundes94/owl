package com.luisfagundes.data.repository

import com.luisfagundes.domain.repository.WifiRepository
import com.luisfagundes.network.wifi.AppWifiManager
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WifiRepositoryImpl @Inject constructor(
    private val wifiManager: AppWifiManager
) : WifiRepository {
    override fun getSsid(): Flow<String?> = flow {
        emit(wifiManager.getCurrentSsid())
    }
}
