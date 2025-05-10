package com.luisfagundes.device.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisfagundes.device.domain.model.Device
import com.luisfagundes.device.domain.usecase.GetDevicesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class DeviceListUiState {
    object Loading : DeviceListUiState()
    data class Success(val devices: List<Device>) : DeviceListUiState()
    data class Error(val throwable: Throwable) : DeviceListUiState()
}

@HiltViewModel
class DeviceListViewModel @Inject constructor(
    private val getDevicesUseCase: GetDevicesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DeviceListUiState>(DeviceListUiState.Loading)
    val uiState: StateFlow<DeviceListUiState> = _uiState

    init {
        getConnectedDevices()
    }

    fun getConnectedDevices() = viewModelScope.launch {
        getDevicesUseCase.invoke()
            .onEach { devices ->
                _uiState.value = DeviceListUiState.Success(devices)
            }
            .catch { error ->
                _uiState.value  = DeviceListUiState.Error(error)
            }
            .collect()
    }
}