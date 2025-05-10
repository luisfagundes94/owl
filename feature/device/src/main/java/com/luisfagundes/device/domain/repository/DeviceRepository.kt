package com.luisfagundes.device.domain.repository

import com.luisfagundes.device.domain.model.Device
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    fun scanDevices(): Flow<List<Device>>
}