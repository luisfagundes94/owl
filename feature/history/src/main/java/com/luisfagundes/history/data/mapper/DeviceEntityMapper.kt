package com.luisfagundes.history.data.mapper

import com.luisfagundes.common.mapper.Mapper
import com.luisfagundes.domain.model.Device
import com.luisfagundes.history.data.model.DeviceEntity

internal class DeviceEntityMapper : Mapper<Device, DeviceEntity> {
    override fun map(source: Device) = DeviceEntity(
        ipAddress = source.ipAddress,
        hostName = source.hostName,
        isActive = source.isActive,
        timestamp = System.currentTimeMillis()
    )
}
