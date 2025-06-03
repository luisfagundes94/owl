package com.luisfagundes.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisfagundes.domain.model.Device
import com.luisfagundes.history.domain.usecase.DeleteDeviceUseCase
import com.luisfagundes.history.domain.usecase.GetSavedDevicesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HistoryUiState {
    data class Success(val devices: List<Device>) : HistoryUiState
    object Empty : HistoryUiState
}

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getSavedDevicesUseCase: GetSavedDevicesUseCase,
    private val deleteDeviceUseCase: DeleteDeviceUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HistoryUiState>(HistoryUiState.Empty)
    val uiState: StateFlow<HistoryUiState> = _uiState

    init {
        getSavedDevices()
    }

    fun getSavedDevices() = viewModelScope.launch {
        getSavedDevicesUseCase.invoke()
            .collect { devices ->
                _uiState.value = if (devices.isEmpty()) {
                    HistoryUiState.Empty
                } else {
                    HistoryUiState.Success(devices)
                }
            }
    }

    fun deleteDevice(device: Device) = viewModelScope.launch(Dispatchers.IO) {
        deleteDeviceUseCase.invoke(device)
    }
}