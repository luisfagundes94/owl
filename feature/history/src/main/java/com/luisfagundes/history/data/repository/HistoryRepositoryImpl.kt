package com.luisfagundes.history.data.repository

import com.luisfagundes.domain.model.Device
import com.luisfagundes.history.data.dao.DeviceDao
import com.luisfagundes.history.data.mapper.DeviceEntityMapper
import com.luisfagundes.history.data.mapper.DeviceMapper
import com.luisfagundes.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class HistoryRepositoryImpl @Inject constructor(
    private val deviceDao: DeviceDao,
    private val deviceMapper: DeviceMapper,
    private val deviceEntityMapper: DeviceEntityMapper
) : HistoryRepository {
    override suspend fun saveDevices(devices: List<Device>) {
        deviceDao.insertDevices(
            deviceEntityList = devices.map { device ->
                deviceEntityMapper.map(device)
            }
        )
    }

    override fun getDevices(): Flow<List<Device>> {
        return deviceDao.getDeviceHistory().map { deviceEntityList ->
            deviceEntityList.map { deviceEntity -> deviceMapper.map(deviceEntity) }
        }
    }
}