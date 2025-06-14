package com.luisfagundes.data.repository

import com.luisfagundes.data.preferences.UserPreferences
import com.luisfagundes.domain.repository.UserRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl @Inject constructor(
    private val userPreferences: UserPreferences
) : UserRepository {
    override fun shouldShowLocationRationale(): Flow<Boolean> =
        userPreferences.shouldShowLocationRationale()

    override suspend fun setShowLocationRationale(shouldShow: Boolean) {
        userPreferences.setShouldShowLocationRationale(shouldShow)
    }
}
