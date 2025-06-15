package com.luisfagundes.history.presentation.wifiList

import androidx.lifecycle.viewModelScope
import com.luisfagundes.common.dispatcher.AppDispatcher
import com.luisfagundes.common.dispatcher.Dispatcher
import com.luisfagundes.common.presentation.ViewModel
import com.luisfagundes.history.domain.usecase.GetWifiRouterListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

@HiltViewModel
class WifiRouterListViewModel @Inject constructor(
    private val getWifiRouterListUseCase: GetWifiRouterListUseCase,
    @Dispatcher(AppDispatcher.IO) private val dispatcher: CoroutineDispatcher
) : ViewModel<WifiRouterListUiState>(
    initialState = WifiRouterListUiState()
) {
    init {
        getWifiRouterList()
    }

    fun getWifiRouterList() = viewModelScope.launch(dispatcher) {
        getWifiRouterListUseCase.invoke().collect { wifiRouterList ->
            updateState { setWifiRouterList(wifiRouterList) }
        }
    }
}
