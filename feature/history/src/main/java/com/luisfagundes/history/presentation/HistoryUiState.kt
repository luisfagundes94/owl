package com.luisfagundes.history.presentation

import com.luisfagundes.common.presentation.UiState
import com.luisfagundes.domain.model.Device

data class HistoryUiState(
    val devices: List<Device> = emptyList()
): UiState {
    fun setDevices(devices: List<Device>) = copy(devices = devices)
}
