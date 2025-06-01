package com.luisfagundes.domain.repository

import kotlinx.coroutines.flow.Flow

interface WifiRepository {
    fun getSsid(): Flow<String?>
}