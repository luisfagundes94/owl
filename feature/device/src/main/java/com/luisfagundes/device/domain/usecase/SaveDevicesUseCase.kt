package com.luisfagundes.device.domain.usecase

import com.luisfagundes.domain.model.Device
import com.luisfagundes.domain.repository.HistoryRepository
import javax.inject.Inject

internal class SaveDevicesUseCase @Inject constructor(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(devices: List<Device>) = historyRepository.saveDevices(devices)
}
