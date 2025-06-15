package com.luisfagundes.history.domain.usecase

import com.luisfagundes.domain.model.WifiRouter
import com.luisfagundes.domain.repository.WifiRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.flow

class GetWifiRouterListUseCase @Inject constructor(
    private val wifiRepository: WifiRepository
) {
    operator fun invoke() = flow {
        val fakeWifiRouterList = listOf(
            WifiRouter(
                ssid = "FAGUNDES_5G",
                devices = emptyList()
            ),
            WifiRouter(
                ssid = "BERALDI",
                devices = emptyList()
            ),
            WifiRouter(
                ssid = "THALITA_2G",
                devices = emptyList()
            )
        )
        emit(fakeWifiRouterList)
    }
}
