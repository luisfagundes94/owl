package com.luisfagundes.history.presentation.wifiList

import com.luisfagundes.common.presentation.UiState
import com.luisfagundes.domain.model.WifiRouter

data class WifiRouterListUiState(
    val wifiRouterList: List<WifiRouter> = emptyList()
) : UiState {
    fun setWifiRouterList(wifiRouterList: List<WifiRouter>) = copy(
        wifiRouterList = wifiRouterList
    )
}
