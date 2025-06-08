package com.luisfagundes.network.wifi

import android.net.wifi.WifiManager
import javax.inject.Inject

@Suppress("DEPRECATION")
class AppWifiManager @Inject constructor(
    private val wifiManager: WifiManager
) {
    fun getCurrentSsid(): String? = try {
        val connectionInfo = wifiManager.connectionInfo
        if (connectionInfo != null) {
            var ssid = connectionInfo.ssid
            if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
                ssid = ssid.substring(1, ssid.length - 1)
            }
            ssid
        } else {
            null
        }
    } catch (_: Exception) {
        null
    }
}
