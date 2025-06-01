package com.luisfagundes.domain.repository

import com.luisfagundes.domain.model.Device
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    suspend fun saveDevices(device: List<Device>)
    fun getDevices(): Flow<List<Device>>
}
