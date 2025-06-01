package com.luisfagundes.device.domain.usecase

import com.luisfagundes.device.domain.repository.DeviceRepository
import com.luisfagundes.domain.model.Device
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

internal class ScanDevicesUseCase @Inject constructor(
    private val repository: DeviceRepository
) {
    operator fun invoke(): Flow<List<Device>> = repository.scanDevices()
}
