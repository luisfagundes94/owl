package com.luisfagundes.device.domain.usecase

import com.luisfagundes.domain.model.Device
import com.luisfagundes.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

internal class SaveDevicesUseCase @Inject constructor(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(scannedDevices: List<Device>) {
        val previouslySavedDeviceList = historyRepository.getDevices().firstOrNull() ?: emptyList()
        val scannedDeviceMap = scannedDevices.associateBy { it.ipAddress }
        val previouslySavedDeviceMap = previouslySavedDeviceList.associateBy { it.ipAddress }
        val allKnownIps = (scannedDeviceMap.keys + previouslySavedDeviceMap.keys)

        val newDeviceStatesToSave = allKnownIps.mapNotNull { ip ->
            val deviceFromScan = scannedDeviceMap[ip]
            val deviceFromCache = previouslySavedDeviceMap[ip]?.copy(isActive = false)
            deviceFromScan ?: deviceFromCache
        }

        historyRepository.saveDevices(newDeviceStatesToSave)
    }
}