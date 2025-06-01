package com.luisfagundes.device.domain.repository

import com.luisfagundes.domain.model.Device
import kotlinx.coroutines.flow.Flow

internal interface DeviceRepository {
    fun scanDevices(): Flow<List<Device>>
}