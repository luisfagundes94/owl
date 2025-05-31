package com.luisfagundes.device.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisfagundes.device.domain.model.Device
import com.luisfagundes.device.domain.usecase.GetDevicesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class DeviceListUiState {
    data class Success(val devices: List<Device> = emptyList()) : DeviceListUiState()
    data class Error(val throwable: Throwable) : DeviceListUiState()
    data object Loading : DeviceListUiState()
}

@HiltViewModel
class DeviceListViewModel @Inject constructor(
    private val getDevicesUseCase: GetDevicesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DeviceListUiState>(DeviceListUiState.Success())
    val uiState: StateFlow<DeviceListUiState> = _uiState

    init {
        getConnectedDevices()
    }

    fun getConnectedDevices() = viewModelScope.launch {
        getDevicesUseCase.invoke()
            .onStart {
                _uiState.value = DeviceListUiState.Loading
            }
            .catch { error ->
                _uiState.value  = DeviceListUiState.Error(error)
            }
            .collect { devices ->
                _uiState.value = DeviceListUiState.Success(devices)
            }
    }
}