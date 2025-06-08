package com.luisfagundes.device.presentation.list

import androidx.lifecycle.viewModelScope
import com.luisfagundes.common.presentation.ViewModel
import com.luisfagundes.device.domain.usecase.SaveDevicesUseCase
import com.luisfagundes.device.domain.usecase.ScanDevicesUseCase
import com.luisfagundes.domain.usecase.GetWifiSsidUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

@HiltViewModel
internal class DeviceListViewModel @Inject constructor(
    private val scanDevicesUseCase: ScanDevicesUseCase,
    private val saveDevicesUseCase: SaveDevicesUseCase,
    private val getWifiSsidUseCase: GetWifiSsidUseCase
) : ViewModel<DeviceListUiState>(
    initialState = DeviceListUiState()
) {
    init {
        scanDevices()
    }

    fun scanDevices() = viewModelScope.launch {
        scanDevicesUseCase.invoke()
            .onStart { updateState { setLoading(true) } }
            .catch { updateState { setError(it) } }
            .collect { devices ->
                updateState { setDevices(devices) }
                saveDevicesUseCase.invoke(devices)
            }
    }

    fun getWifiSsid() = viewModelScope.launch {
        getWifiSsidUseCase.invoke().collect { ssid ->
            updateState { setWifiName(ssid) }
        }
    }
}
