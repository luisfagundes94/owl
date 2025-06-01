package com.luisfagundes.device.usecase

import com.luisfagundes.device.domain.usecase.GetWifiSsidUseCase
import com.luisfagundes.domain.repository.WifiRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetWifiSsidUseCaseTest {

    private val repository: WifiRepository = mockk()
    private val useCase = GetWifiSsidUseCase(repository)

    @Test
    fun `returns ssid when repository returns ssid`() = runTest {
        val ssid = "MyWifi"
        every { repository.getSsid() } returns flowOf(ssid)

        val result = useCase().toList()

        assertEquals(listOf(ssid), result)
    }

    @Test
    fun `returns empty when repository returns empty flow`() = runTest {
        every { repository.getSsid() } returns flowOf()

        val result = useCase().toList()

        assertEquals(emptyList<String>(), result)
    }

    @Test
    fun `returns null when repository returns null ssid`() = runTest {
        every { repository.getSsid() } returns flowOf(null)

        val result = useCase().toList()

        assertEquals(listOf<String?>(null), result)
    }
}