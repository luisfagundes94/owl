package com.luisfagundes.history.viewmodel

import app.cash.turbine.test
import com.luisfagundes.domain.model.Device
import com.luisfagundes.history.domain.usecase.DeleteDeviceUseCase
import com.luisfagundes.history.domain.usecase.GetSavedDevicesUseCase
import com.luisfagundes.history.presentation.HistoryUiState
import com.luisfagundes.history.presentation.HistoryViewModel
import com.luisfagundes.testing.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getSavedDevicesUseCase: GetSavedDevicesUseCase = mockk()
    private val deleteDeviceUseCase: DeleteDeviceUseCase = mockk(relaxed = true)

    private val initialUiState = HistoryUiState()
    private val devices = listOf(Device("192.168.1.2", "host", true))

    private lateinit var viewModel: HistoryViewModel

    @Before
    fun setUp() {
        coEvery { getSavedDevicesUseCase.invoke() } returns flowOf(emptyList())

        viewModel = HistoryViewModel(
            getSavedDevicesUseCase,
            deleteDeviceUseCase
        )
    }

    @Test
    fun `init calls getSavedDevices`() = runTest {
        coVerify { getSavedDevicesUseCase.invoke() }
    }

    @Test
    fun `getSavedDevices updates state with devices`() = runTest {
        // Given
        coEvery { getSavedDevicesUseCase.invoke() } returns flowOf(devices)

        // When
        viewModel.getSavedDevices()

        // Then
        viewModel.uiState.test {
            assertEquals(initialUiState, awaitItem())
            assertEquals(initialUiState.setDevices(devices), awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `deleteDevice calls deleteDeviceUseCase`() = runTest {
        // Given
        val device = Device("192.168.1.2", "host", true)

        // When
        viewModel.deleteDevice(device)

        // Then
        coVerify { deleteDeviceUseCase.invoke(device) }
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
