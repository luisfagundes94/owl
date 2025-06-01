package com.luisfagundes.network.wifi

import android.net.wifi.WifiManager
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Suppress("DEPRECATION")
class AppWifiManager @Inject constructor(
    private val wifiManager: WifiManager
) {
    private val _ssidName = MutableStateFlow<String?>(null)
    val ssidName: Flow<String?> = _ssidName

    init {
        refreshSsid()
    }

    fun refreshSsid() {
        val wifiInfo = wifiManager.connectionInfo
        val ssid = wifiInfo.ssid
        _ssidName.value = ssid?.removePrefix("\"")?.removeSuffix("\"")
    }
}
