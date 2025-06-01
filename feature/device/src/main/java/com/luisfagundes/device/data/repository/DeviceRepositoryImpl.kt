package com.luisfagundes.device.data.repository

import com.luisfagundes.device.domain.repository.DeviceRepository
import com.luisfagundes.domain.model.Device
import com.luisfagundes.device.data.scanner.DeviceScanner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DeviceRepositoryImpl @Inject constructor(
    private val scanner: DeviceScanner
) : DeviceRepository {
    override fun scanDevices(): Flow<List<Device>> = flow {
        val devices = scanner.scanAll()
        emit(devices)
    }.flowOn(Dispatchers.IO)
}