package com.luisfagundes.history.domain.usecase

import com.luisfagundes.domain.model.Device
import com.luisfagundes.domain.repository.HistoryRepository
import javax.inject.Inject

class DeleteDeviceUseCase @Inject constructor(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(device: Device) {
        repository.deleteDevice(device)
    }
}
