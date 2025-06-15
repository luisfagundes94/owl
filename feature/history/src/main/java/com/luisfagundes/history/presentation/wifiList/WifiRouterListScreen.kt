package com.luisfagundes.history.presentation.wifiList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.luisfagundes.designsystem.theme.spacing
import com.luisfagundes.domain.model.WifiRouter
import com.luisfagundes.history.R
import com.luisfagundes.history.presentation.wifiList.components.WifiRouterCard

@Composable
internal fun HistoryRoute(viewModel: WifiRouterListViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HistoryScreen(
        uiState = uiState,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun HistoryScreen(uiState: WifiRouterListUiState, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
    ) {
        @Suppress("KotlinConstantConditions")
        when {
            uiState.wifiRouterList.isNotEmpty() -> WifiRouterList(
                wifiRouterList = uiState.wifiRouterList,
                modifier = Modifier.fillMaxWidth()
            )

            uiState.wifiRouterList.isEmpty() -> EmptyMessage(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun EmptyMessage(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.no_history_available),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun WifiRouterList(wifiRouterList: List<WifiRouter>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
    ) {
        stickyHeader {
            Text(
                text = stringResource(R.string.wifi_router_history),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.default)
            )
        }
        itemsIndexed(
            items = wifiRouterList,
            key = { _, wifiRouter -> wifiRouter.ssid }
        ) { index, wifiRouter ->
            WifiRouterCard(
                name = wifiRouter.ssid,
                modifier = Modifier
                    .clickable {}
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.default)
            )
            if (index != wifiRouterList.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.default)
                )
            }
        }
    }
}
