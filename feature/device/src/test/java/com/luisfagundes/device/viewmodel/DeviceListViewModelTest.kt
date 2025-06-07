package com.luisfagundes.device.viewmodel

import app.cash.turbine.test
import com.luisfagundes.device.domain.usecase.SaveDevicesUseCase
import com.luisfagundes.device.domain.usecase.ScanDevicesUseCase
import com.luisfagundes.device.presentation.list.DeviceListUiState
import com.luisfagundes.device.presentation.list.DeviceListViewModel
import com.luisfagundes.domain.model.Device
import com.luisfagundes.testing.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DeviceListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val scanDevicesUseCase: ScanDevicesUseCase = mockk()
    private val saveDevicesUseCase: SaveDevicesUseCase = mockk()

    @Test
    fun `emits Loading then Success when scan succeeds`() = runTest {
        // Given
        val devices = listOf(Device("192.168.1.2", "host", true))
        coEvery { scanDevicesUseCase.invoke() } returns flow { emit(devices) }

        // When
        val viewModel = DeviceListViewModel(scanDevicesUseCase, saveDevicesUseCase)

        // Then
        viewModel.uiState.test {
            assert(awaitItem() is DeviceListUiState.Loading)

            val success = awaitItem() as DeviceListUiState.Success

            assert(success.devices == devices)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits Loading then Error when scan fails`() = runTest {
        // Given
        val throwable = RuntimeException("Scan failed")
        coEvery { scanDevicesUseCase.invoke() } returns flow { throw throwable }

        // When
        val viewModel = DeviceListViewModel(scanDevicesUseCase, saveDevicesUseCase)

        // Then
        viewModel.uiState.test {
            assert(awaitItem() is DeviceListUiState.Loading)

            val error = awaitItem() as DeviceListUiState.Error

            assert(error.throwable == throwable)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
