package com.luisfagundes.history.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.luisfagundes.designsystem.component.DeviceCard
import com.luisfagundes.designsystem.theme.spacing
import com.luisfagundes.domain.model.Device
import com.luisfagundes.history.R

@Composable
internal fun HistoryRoute(
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HistoryScreen(
        modifier = Modifier.fillMaxSize(),
        uiState = uiState,
        onDeleteDevice = viewModel::deleteDevice,
    )
}

@Composable
private fun HistoryScreen(
    modifier: Modifier = Modifier,
    uiState: HistoryUiState,
    onDeleteDevice: (Device) -> Unit,
) {
    Box(
        modifier = modifier,
    ) {
        when (uiState) {
            is HistoryUiState.Success -> SavedDevices(
                modifier = Modifier.fillMaxWidth(),
                devices = uiState.devices,
                onDeleteDevice = onDeleteDevice,
            )

            is HistoryUiState.Empty -> EmptyMessage(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun EmptyMessage(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.no_history_available),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun SavedDevices(
    modifier: Modifier = Modifier,
    devices: List<Device>,
    onDeleteDevice: (Device) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        contentPadding = PaddingValues(MaterialTheme.spacing.default)
    ) {
        stickyHeader {
            Text(
                text = stringResource(R.string.device_history),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = MaterialTheme.spacing.default),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        items(devices, key = { it.ipAddress }) { device ->
            val swipeState = rememberSwipeToDismissBoxState()

            SwipeToDismissBox(
                modifier = Modifier.animateContentSize(),
                state = swipeState,
                enableDismissFromStartToEnd = false,
                backgroundContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = MaterialTheme.colorScheme.errorContainer,
                                shape = MaterialTheme.shapes.medium
                            )
                            .padding(MaterialTheme.spacing.small),
                        contentAlignment = Alignment.CenterEnd,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete_device),
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            ) {
                DeviceCard(
                    hostName = device.hostName,
                    ipAddress = device.ipAddress,
                    isActive = device.isActive
                )
            }

            when (swipeState.currentValue) {
                SwipeToDismissBoxValue.EndToStart -> {
                    LaunchedEffect(device.ipAddress) {
                        onDeleteDevice(device)
                    }
                }

                else -> Unit
            }
        }
    }
}

