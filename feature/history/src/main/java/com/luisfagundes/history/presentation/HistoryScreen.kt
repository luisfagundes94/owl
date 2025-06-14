package com.luisfagundes.history.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.luisfagundes.domain.model.Device
import com.luisfagundes.history.R
import com.luisfagundes.history.presentation.components.DeviceCardWithSwipe

@Composable
internal fun HistoryRoute(viewModel: HistoryViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HistoryScreen(
        uiState = uiState,
        onDeleteDevice = viewModel::deleteDevice,
        onDeleteAll = {},
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun HistoryScreen(
    uiState: HistoryUiState,
    onDeleteDevice: (Device) -> Unit,
    onDeleteAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        when {
            uiState.devices.isNotEmpty() -> SavedDevices(
                devices = uiState.devices,
                onDeleteDevice = onDeleteDevice,
                onDeleteAll = onDeleteAll,
                modifier = Modifier.fillMaxWidth()
            )

            uiState.devices.isEmpty() -> EmptyMessage(
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
private fun SavedDevices(
    devices: List<Device>,
    onDeleteDevice: (Device) -> Unit,
    onDeleteAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        stickyHeader {
            DeviceListHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.default),
                onDeleteAll = onDeleteAll
            )
        }
        itemsIndexed(
            items = devices,
            key = { _, device -> device.ipAddress }
        ) { index, device ->
            DeviceCardWithSwipe(
                device = device,
                onDeleteDevice = onDeleteDevice,
                modifier = Modifier
                    .animateContentSize()
                    .animateItem()
            )
            if (index != devices.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.default)
                )
            }
        }
    }
}

@Composable
private fun DeviceListHeader(onDeleteAll: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.device_history),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        IconButton(
            onClick = onDeleteAll
        ) {
            Icon(
                imageVector = Icons.Default.ClearAll,
                contentDescription = stringResource(R.string.clear_all_history)
            )
        }
    }
}
