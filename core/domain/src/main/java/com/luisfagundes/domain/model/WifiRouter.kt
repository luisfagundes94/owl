package com.luisfagundes.domain.model

data class WifiRouter(
    val ssid: String,
    val devices: List<Device>
)
