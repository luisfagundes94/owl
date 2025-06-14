package com.luisfagundes.device.viewmodel

import app.cash.turbine.test
import com.luisfagundes.device.domain.usecase.SaveDevicesUseCase
import com.luisfagundes.device.domain.usecase.ScanDevicesUseCase
import com.luisfagundes.device.presentation.list.DeviceListUiState
import com.luisfagundes.device.presentation.list.DeviceListViewModel
import com.luisfagundes.domain.model.Device
import com.luisfagundes.domain.repository.UserRepository
import com.luisfagundes.domain.usecase.GetWifiSsidUseCase
import com.luisfagundes.testing.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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
    private val userRepository: UserRepository = mockk()

    private val initialUiState = DeviceListUiState()
    private val devices = listOf(Device("192.168.1.2", "host", true))

    private lateinit var viewModel: DeviceListViewModel

    @Before
    fun setUp() {
        coEvery { scanDevicesUseCase.invoke() } returns flowOf(initialUiState.devices)
        coEvery { saveDevicesUseCase.invoke(any()) } returns Unit
        coEvery { userRepository.setShowLocationRationale(any()) } returns Unit
        coEvery { userRepository.shouldShowLocationRationale() } returns flowOf(
            initialUiState.shouldShowPermissionRationale
        )

        viewModel = DeviceListViewModel(
            scanDevicesUseCase,
            saveDevicesUseCase,
            getWifiSsidUseCase,
            userRepository
        )
    }

    @Test
    fun `scan success updates state with devices`() = runTest {
        // Given
        coEvery { scanDevicesUseCase.invoke() } returns flowOf(devices)

        // When & Then
        viewModel.uiState.test {
            assertEquals(initialUiState, awaitItem())

            viewModel.scanDevices()

            assertEquals(initialUiState.setLoading(true), awaitItem())
            assertEquals(initialUiState.setDevices(devices), awaitItem())

            coVerify { saveDevicesUseCase.invoke(devices) }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `scan failure updates state with error`() = runTest {
        // Given
        val scanError = RuntimeException("Scan failed")
        coEvery { scanDevicesUseCase.invoke() } returns flow { throw scanError }

        // When & Then
        viewModel.uiState.test {
            assertEquals(initialUiState, awaitItem())

            viewModel.scanDevices()

            assertEquals(initialUiState.setLoading(true), awaitItem())
            assertEquals(initialUiState.setError(scanError), awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getWifiSsid updates state with wifi name`() = runTest {
        // Given
        val testSsid = "MyWifi"
        coEvery { getWifiSsidUseCase.invoke() } returns flowOf(testSsid)

        // When & Then
        viewModel.uiState.test {
            assertEquals(initialUiState, awaitItem())

            viewModel.getWifiSsid()

            assertEquals(initialUiState.setWifiName(testSsid), awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadPermissionRationale updates state correctly`() = runTest {
        // Given
        val shouldShowRationale = true
        coEvery { userRepository.shouldShowLocationRationale() } returns
                flowOf(shouldShowRationale)

        // When & Then
        viewModel.uiState.test {
            assertEquals(initialUiState, awaitItem())

            viewModel.loadPermissionRationaleState()

            assertEquals(
                initialUiState.setShowLocationRationale(shouldShowRationale),
                awaitItem()
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `dismissing permission rationale with dontAskAgain updates repository`() = runTest {
        // Given
        val booleanSlot = slot<Boolean>()
        coEvery { userRepository.setShowLocationRationale(capture(booleanSlot)) } returns Unit

        // When
        viewModel.onPermissionRationaleDismissed(dontAskAgain = true)
        advanceUntilIdle()

        // Then
        coVerify { userRepository.setShowLocationRationale(false) }
        assertEquals(false, booleanSlot.captured)

        viewModel.uiState.test {
            assertEquals(initialUiState.setShowLocationRationale(false), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `dismissing permission rationale without dontAskAgain only hides UI`() = runTest {
        // When
        viewModel.onPermissionRationaleDismissed(dontAskAgain = false)
        advanceUntilIdle()

        // Then
        coVerify(exactly = 0) { userRepository.setShowLocationRationale(any()) }

        viewModel.uiState.test {
            assertEquals(initialUiState.setShowLocationRationale(false), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `hidePermissionRationale updates UI state`() = runTest {
        // Given
        coEvery { userRepository.shouldShowLocationRationale() } returns flowOf(true)

        // When & Then
        viewModel.hidePermissionRationale()
        advanceUntilIdle()

        viewModel.uiState.test {
            assertEquals(initialUiState.setShowLocationRationale(false), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `initial state has default values`() = runTest {
        // When & Then
        viewModel.uiState.test {
            assertEquals(initialUiState, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
