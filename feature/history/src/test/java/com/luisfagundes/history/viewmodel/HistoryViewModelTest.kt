package com.luisfagundes.history.viewmodel

import app.cash.turbine.test
import com.luisfagundes.domain.model.WifiRouter
import com.luisfagundes.history.domain.usecase.DeleteDeviceUseCase
import com.luisfagundes.history.domain.usecase.GetWifiRouterListUseCase
import com.luisfagundes.history.presentation.wifiList.WifiRouterListUiState
import com.luisfagundes.history.presentation.wifiList.WifiRouterListViewModel
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

    private val getWifiRouterListUseCase = mockk<GetWifiRouterListUseCase>(relaxed = true)
    private val deleteDeviceUseCase: DeleteDeviceUseCase = mockk(relaxed = true)

    private val initialUiState = WifiRouterListUiState()
    private val wifiRouterList = listOf(
        WifiRouter(
            ssid = "FAGUNDES_5G",
            devices = emptyList()
        ),
        WifiRouter(
            ssid = "BERALDI",
            devices = emptyList()
        )
    )

    private lateinit var viewModel: WifiRouterListViewModel

    @Before
    fun setUp() {
        coEvery { getWifiRouterListUseCase.invoke() } returns flowOf(emptyList())

        viewModel = WifiRouterListViewModel(
            getWifiRouterListUseCase = getWifiRouterListUseCase,
            dispatcher = mainDispatcherRule.testDispatcher
        )
    }

    @Test
    fun `init calls getWifiRouterList`() = runTest {
        coVerify { getWifiRouterListUseCase.invoke() }
    }

    @Test
    fun `getWifiRouterList updates state with wifi router list`() = runTest {
        // Given
        coEvery { getWifiRouterListUseCase.invoke() } returns flowOf(wifiRouterList)

        // When
        viewModel.getWifiRouterList()

        // Then
        viewModel.uiState.test {
            assertEquals(initialUiState, awaitItem())
            assertEquals(initialUiState.setWifiRouterList(wifiRouterList), awaitItem())

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
