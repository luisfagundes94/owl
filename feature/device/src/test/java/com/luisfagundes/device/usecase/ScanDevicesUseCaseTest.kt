package com.luisfagundes.device.usecase

import com.luisfagundes.device.domain.repository.DeviceRepository
import com.luisfagundes.device.domain.usecase.ScanDevicesUseCase
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
    fun returnsListOfDevicesWhenRepositoryReturnsDevices() = runTest {
        val devices = listOf(Device(
            ipAddress = "123123",
            hostName = "host1",
        ), Device(
            ipAddress = "1231234",
            hostName = "host2",
        ))
        every {  repository.scanDevices() } returns flowOf(devices)

        val result = useCase().toList()

        assertEquals(devices, result.first())
    }

    @Test
    fun returnsEmptyListWhenRepositoryReturnsEmptyList() = runTest {
        every { repository.scanDevices() } returns flowOf(emptyList())

        val result = useCase().toList()

        assertEquals(emptyList<Device>(), result.first())
    }
}