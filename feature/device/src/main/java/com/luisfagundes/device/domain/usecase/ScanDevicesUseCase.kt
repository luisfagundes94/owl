package com.luisfagundes.device.domain.usecase

import com.luisfagundes.device.domain.repository.DeviceRepository
import com.luisfagundes.domain.model.Device
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ScanDevicesUseCase @Inject constructor(
    private val repository: DeviceRepository
) {
    operator fun invoke(): Flow<List<Device>> = repository.scanDevices()
}