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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DeviceListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val scanDevicesUseCase: ScanDevicesUseCase = mockk()
    private val saveDevicesUseCase: SaveDevicesUseCase = mockk(relaxed = true)
    private val getWifiSsidUseCase: GetWifiSsidUseCase = mockk()
    private val userRepository: UserRepository = mockk()

    private val defaultUiState = DeviceListUiState()

    private lateinit var viewModel: DeviceListViewModel

    @Before
    fun setUp() {
        coEvery { userRepository.shouldShowLocationRationale() } returns flowOf(
            defaultUiState.shouldShowPermissionRationale
        )
        coEvery { scanDevicesUseCase.invoke() } returns flowOf(
            defaultUiState.devices
        )
        coEvery { saveDevicesUseCase.invoke(any()) } returns Unit

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
            val testDevices = listOf(Device("192.168.1.2", "host", true))
            coEvery { scanDevicesUseCase.invoke() } returns flow { emit(testDevices) }
            coEvery { saveDevicesUseCase.invoke(testDevices) } returns Unit

            viewModel.uiState.test {
                // Then
                assertEquals(defaultUiState, awaitItem())

                // When
                viewModel.scanDevices()

                // Then
                val expectedLoadingState = defaultUiState.setLoading(true)
                assertEquals(expectedLoadingState, awaitItem())

                // Then
                val expectedSuccessState = defaultUiState.setDevices(testDevices)
                assertEquals(expectedSuccessState, awaitItem())

                // Then
                coVerify { saveDevicesUseCase.invoke(testDevices) }

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `when scan fails, should update state with error`() = runTest {
        // Given
        val scanError = RuntimeException("Scan failed")
        coEvery { scanDevicesUseCase.invoke() } returns flow { throw scanError }

        viewModel.uiState.test {
            // Then
            assertEquals(defaultUiState, awaitItem())

            // When
            viewModel.scanDevices()

            // Then
            val expectedLoadingState = defaultUiState.setLoading(true)
            assertEquals(expectedLoadingState, awaitItem())

            // Then
            val expectedErrorState = defaultUiState.setError(scanError)
            assertEquals(expectedErrorState, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when getWifiSsid is called, should update state with wifi name`() = runTest {
        // Given
        val testSsid = "MyWifi"
        coEvery { getWifiSsidUseCase.invoke() } returns flowOf(testSsid)

        viewModel.uiState.test {
            // Then
            assertEquals(defaultUiState, awaitItem())

            // When
            viewModel.getWifiSsid()

            // Then
            val expectedSsidState = defaultUiState.setWifiName(testSsid)
            assertEquals(expectedSsidState, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }
}
