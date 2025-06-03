package com.luisfagundes.history.domain.usecase

import com.luisfagundes.domain.repository.HistoryRepository
import javax.inject.Inject

class GetSavedDevicesUseCase @Inject constructor(
    private val repository: HistoryRepository
) {
    operator fun invoke() = repository.getDevices()
}