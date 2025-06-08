package com.luisfagundes.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun shouldShowLocationRationale(): Flow<Boolean>
    suspend fun setShowLocationRationale(shouldShow: Boolean)
}