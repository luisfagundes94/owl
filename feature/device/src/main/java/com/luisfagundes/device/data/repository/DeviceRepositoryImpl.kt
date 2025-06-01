package com.luisfagundes.device.data.repository

import com.luisfagundes.device.data.scanner.DeviceScanner
import com.luisfagundes.domain.repository.DeviceRepository
import com.luisfagundes.domain.model.Device
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

internal class DeviceRepositoryImpl @Inject constructor(
    private val scanner: DeviceScanner
) : DeviceRepository {
    override fun scanDevices(): Flow<List<Device>> = flow {
        val devices = scanner.scanAll()
        emit(devices)
    }.flowOn(Dispatchers.IO)
}
