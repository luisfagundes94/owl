package com.luisfagundes.history.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.luisfagundes.history.data.model.DeviceEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface DeviceDao {
    @Insert
    suspend fun insertDevices(deviceEntityList: List<DeviceEntity>)

    @Query("SELECT * FROM device ORDER BY timestamp DESC")
    fun getDeviceHistory(): Flow<List<DeviceEntity>>
}