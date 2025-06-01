package com.luisfagundes.history.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.luisfagundes.history.data.dao.DeviceDao
import com.luisfagundes.history.data.model.DeviceEntity

@Database(
    entities = [DeviceEntity::class],
    version = 1,
    exportSchema = false
)
internal abstract class HistoryDatabase : RoomDatabase() {
    abstract fun deviceDao(): DeviceDao
}
