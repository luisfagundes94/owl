package com.luisfagundes.data.repository

import com.luisfagundes.data.preferences.UserPreferences
import com.luisfagundes.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userPreferences: UserPreferences
) : UserRepository {
    override fun shouldShowLocationRationale(): Flow<Boolean> {
        return userPreferences.shouldShowLocationRationale()
    }

    override suspend fun setShowLocationRationale(shouldShow: Boolean) {
        userPreferences.setShouldShowLocationRationale(shouldShow)
    }
}