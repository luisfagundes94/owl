package com.luisfagundes.device.viewmodel

import app.cash.turbine.test
import com.luisfagundes.device.domain.usecase.SaveDevicesUseCase
import com.luisfagundes.device.domain.usecase.ScanDevicesUseCase
import com.luisfagundes.device.presentation.list.DeviceListViewModel
import com.luisfagundes.domain.model.Device
import com.luisfagundes.domain.repository.UserRepository
import com.luisfagundes.domain.usecase.GetWifiSsidUseCase
import com.luisfagundes.testing.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DeviceListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val scanDevicesUseCase: ScanDevicesUseCase = mockk(relaxed = true)
    private val saveDevicesUseCase: SaveDevicesUseCase = mockk(relaxed = true)
    private val getWifiSsidUseCase: GetWifiSsidUseCase = mockk(relaxed = true)
    private val userRepository: UserRepository = mockk(relaxed = true)

    private lateinit var viewModel: DeviceListViewModel

    @Before
    fun setUp() {
        viewModel = DeviceListViewModel(
            scanDevicesUseCase,
            saveDevicesUseCase,
            getWifiSsidUseCase,
            userRepository
        )
    }

    @Test
    fun `when scan succeeds, should update state with devices`() = runTest {
        // Given
        val devices = listOf(Device("192.168.1.2", "host", true))
        coEvery { scanDevicesUseCase.invoke() } returns flow { emit(devices) }

        // When
        viewModel.scanDevices()

        // Then
        viewModel.uiState.test {
            val initialState = awaitItem()
            assertTrue(initialState.isLoading)

            val finalState = awaitItem()
            assertEquals(devices, finalState.devices)
            assertFalse(finalState.isLoading)
            assertNull(finalState.error)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when scan fails, should update state with error`() = runTest {
        // Given
        val throwable = RuntimeException("Scan failed")
        coEvery { scanDevicesUseCase.invoke() } returns flow { throw throwable }

        // When
        viewModel.scanDevices()

        // Then
        viewModel.uiState.test {
            val initialState = awaitItem()
            assertTrue(initialState.isLoading)

            val errorState = awaitItem()
            assertEquals(throwable, errorState.error)
            assertFalse(errorState.isLoading)
            assertEquals(emptyList<Device>(), errorState.devices)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when getWifiSsid is called, should update state with wifi name`() = runTest {
        // Given
        val ssid = "MyWifi"
        coEvery { getWifiSsidUseCase.invoke() } returns flow { emit(ssid) }

        // When
        viewModel.getWifiSsid()

        // Then
        viewModel.uiState.test {
            skipItems(1) // Skip initial state emission

            assertEquals(ssid, awaitItem().wifiName)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
