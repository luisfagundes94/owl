package com.luisfagundes.history.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.luisfagundes.history.data.model.DeviceEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface DeviceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevices(deviceEntityList: List<DeviceEntity>)

    @Query("SELECT * FROM device ORDER BY timestamp DESC")
    fun getDeviceHistory(): Flow<List<DeviceEntity>>

    @Delete
    fun deleteDevice(deviceEntity: DeviceEntity)
}
