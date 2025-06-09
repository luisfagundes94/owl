package com.luisfagundes.domain.usecase

import com.luisfagundes.domain.repository.WifiRepository
import javax.inject.Inject

class GetWifiSsidUseCase @Inject constructor(
    private val repository: WifiRepository
) {
    operator fun invoke() = repository.getSsid()
}
