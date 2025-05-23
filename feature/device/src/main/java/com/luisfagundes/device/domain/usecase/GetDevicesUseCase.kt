package com.luisfagundes.device.domain.usecase

import com.luisfagundes.device.domain.model.Device
import com.luisfagundes.device.domain.repository.DeviceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDevicesUseCase @Inject constructor(
    private val repository: DeviceRepository
) {
    operator fun invoke(): Flow<List<Device>> = repository.scanDevices()
}