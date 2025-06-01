package com.luisfagundes.history.data.mapper

import com.luisfagundes.common.mapper.Mapper
import com.luisfagundes.domain.model.Device
import com.luisfagundes.history.data.model.DeviceEntity

internal class DeviceMapper : Mapper<DeviceEntity, Device> {
    override fun map(source: DeviceEntity): Device {
        return Device(
            ipAddress = source.ipAddress,
            hostName = source.hostName,
            isActive = source.isActive
        )
    }
}
