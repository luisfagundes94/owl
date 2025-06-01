package com.luisfagundes.domain.usecase

import com.luisfagundes.domain.model.Device
import com.luisfagundes.domain.repository.DeviceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ScanDevicesUseCase @Inject constructor(
    private val repository: DeviceRepository
) {
    operator fun invoke(): Flow<List<Device>> = repository.scanDevices()
}