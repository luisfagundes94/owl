package com.luisfagundes.device.data.scanner

import com.luisfagundes.domain.model.Device

interface DeviceScanner {
    suspend fun scanAll(): List<Device>
}