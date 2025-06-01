package com.luisfagundes.device.usecase

import com.luisfagundes.domain.repository.DeviceRepository
import com.luisfagundes.domain.usecase.ScanDevicesUseCase
import com.luisfagundes.domain.model.Device
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ScanDevicesUseCaseTest {

    private val repository: DeviceRepository = mockk(relaxed = true)
    private val useCase = ScanDevicesUseCase(repository)

    @Test
    fun `returns list of devices when repository returns devices`() = runTest {
        val devices = listOf(
            Device(
                ipAddress = "123123",
                hostName = "host1",
                isActive = true
            ),
            Device(
                ipAddress = "1231234",
                hostName = "host2",
                isActive = false
            )
        )
        every { repository.scanDevices() } returns flowOf(devices)

        val result = useCase().toList()

        assertEquals(devices, result.first())
    }

    @Test
    fun `returns empty list when repository returns empty list`() = runTest {
        every { repository.scanDevices() } returns flowOf(emptyList())

        val result = useCase().toList()

        assertEquals(emptyList<Device>(), result.first())
    }
}
