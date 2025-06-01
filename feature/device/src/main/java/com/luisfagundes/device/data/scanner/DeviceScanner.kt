package com.luisfagundes.device.data.scanner

import com.luisfagundes.domain.model.Device

internal interface DeviceScanner {
    suspend fun scanAll(): List<Device>
}