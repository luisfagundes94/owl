package com.luisfagundes.device.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisfagundes.device.domain.usecase.ScanDevicesUseCase
import com.luisfagundes.domain.model.Device
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

internal sealed class DeviceListUiState {
    data class Success(val devices: List<Device> = emptyList()) : DeviceListUiState()
    data class Error(val throwable: Throwable) : DeviceListUiState()
    data object Loading : DeviceListUiState()
}

@HiltViewModel
internal class DeviceListViewModel @Inject constructor(
    private val scanDevicesUseCase: ScanDevicesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DeviceListUiState>(DeviceListUiState.Loading)
    val uiState: StateFlow<DeviceListUiState> = _uiState

    init {
        scanDevices()
    }

    fun scanDevices() = viewModelScope.launch {
        scanDevicesUseCase.invoke()
            .onStart {
                _uiState.value = DeviceListUiState.Loading
            }
            .catch { error ->
                _uiState.value = DeviceListUiState.Error(error)
            }
            .collect { devices ->
                _uiState.value = DeviceListUiState.Success(devices)
            }
    }
}
