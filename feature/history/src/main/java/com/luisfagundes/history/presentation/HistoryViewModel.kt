package com.luisfagundes.history.presentation

import androidx.lifecycle.viewModelScope
import com.luisfagundes.common.presentation.ViewModel
import com.luisfagundes.domain.model.Device
import com.luisfagundes.history.domain.usecase.DeleteDeviceUseCase
import com.luisfagundes.history.domain.usecase.GetSavedDevicesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getSavedDevicesUseCase: GetSavedDevicesUseCase,
    private val deleteDeviceUseCase: DeleteDeviceUseCase
) : ViewModel<HistoryUiState>(
    initialState = HistoryUiState()
) {
    init {
        getSavedDevices()
    }

    fun getSavedDevices() = viewModelScope.launch {
        getSavedDevicesUseCase.invoke()
            .collect { devices ->
                updateState { setDevices(devices) }
            }
    }

    fun deleteDevice(device: Device) = viewModelScope.launch(Dispatchers.IO) {
        deleteDeviceUseCase.invoke(device)
    }
}
