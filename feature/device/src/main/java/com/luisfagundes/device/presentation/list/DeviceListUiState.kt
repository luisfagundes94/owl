package com.luisfagundes.device.presentation.list

import com.luisfagundes.common.presentation.UiState
import com.luisfagundes.domain.model.Device

internal data class DeviceListUiState(
    val devices: List<Device> = emptyList(),
    val wifiName: String? = null,
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val shouldShowPermissionRationale: Boolean = false
) : UiState {
    fun setDevices(devices: List<Device>) = copy(
        devices = devices,
        isLoading = false,
        error = null
    )

    fun setWifiName(wifiName: String?) = copy(
        wifiName = wifiName,
        shouldShowPermissionRationale = false
    )

    fun setLoading(isLoading: Boolean) = copy(
        isLoading = isLoading,
        error = null
    )

    fun setError(error: Throwable?) = copy(
        error = error,
        isLoading = false
    )

    fun setShowLocationRationale(showLocationRationale: Boolean) = copy(
        shouldShowPermissionRationale = showLocationRationale
    )
}
