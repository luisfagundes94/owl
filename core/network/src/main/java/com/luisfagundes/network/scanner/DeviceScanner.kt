package com.luisfagundes.network.scanner

import com.luisfagundes.domain.model.Device

interface DeviceScanner {
    suspend fun scanAll(): List<Device>
}