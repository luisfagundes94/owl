package com.luisfagundes.history.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device")
data class DeviceEntity(
    @PrimaryKey val ipAddress: String,
    val hostName: String,
    val isActive: Boolean,
    val timestamp: Long
)
