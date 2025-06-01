package com.luisfagundes.device.domain.usecase

import com.luisfagundes.domain.repository.WifiRepository
import javax.inject.Inject

internal class GetWifiSsidUseCase @Inject constructor(
    private val repository: WifiRepository
) {
    operator fun invoke() = repository.getSsid()
}