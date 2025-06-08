package com.luisfagundes.device.presentation.list

import androidx.lifecycle.viewModelScope
import com.luisfagundes.common.presentation.ViewModel
import com.luisfagundes.device.domain.usecase.SaveDevicesUseCase
import com.luisfagundes.device.domain.usecase.ScanDevicesUseCase
import com.luisfagundes.domain.repository.UserRepository
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
    private val getWifiSsidUseCase: GetWifiSsidUseCase,
    private val userRepository: UserRepository
) : ViewModel<DeviceListUiState>(
    initialState = DeviceListUiState()
) {
    init {
        loadPermissionRationaleState()
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

    fun loadPermissionRationaleState() = viewModelScope.launch {
        userRepository.shouldShowLocationRationale().collect { shouldShow ->
            updateState { setShowLocationRationale(shouldShow) }
        }
    }

    fun onPermissionRationaleDismissed(dontAskAgain: Boolean) = viewModelScope.launch {
        if (dontAskAgain) {
            userRepository.setShowLocationRationale(false)
        }
        hidePermissionRationale()
    }

    fun hidePermissionRationale() {
        updateState { setShowLocationRationale(false) }
    }
}
