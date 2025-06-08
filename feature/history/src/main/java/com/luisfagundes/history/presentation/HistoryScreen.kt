package com.luisfagundes.history.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.lerp as lerpColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.util.lerp as lerpFloat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.luisfagundes.designsystem.component.DeviceCard
import com.luisfagundes.designsystem.theme.spacing
import com.luisfagundes.domain.model.Device
import com.luisfagundes.history.R
import com.luisfagundes.history.extensions.isEndToStartDirection
import com.luisfagundes.history.extensions.isSettledDirection

private const val SWIPE_THRESHOLD = 0.3f

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
                onDeleteDevice = onDeleteDevice
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

@Composable
private fun LazyItemScope.DeviceCardWithSwipe(device: Device, onDeleteDevice: (Device) -> Unit) {
    val currentOnDeleteDevice = rememberUpdatedState(onDeleteDevice)
    val swipeState = rememberSwipeToDismissBoxState()
    val swipeProgress = if (swipeState.isSettledDirection) {
        0f
    } else {
        (swipeState.progress / SWIPE_THRESHOLD).coerceIn(0f, 1f)
    }

    val backgroundColor = lerpColor(
        start = MaterialTheme.colorScheme.background,
        stop = if (swipeState.isEndToStartDirection) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.background
        },
        fraction = swipeProgress
    )

    val iconScale = lerpFloat(
        start = 0f,
        stop = 1f,
        fraction = swipeProgress
    )

    SwipeToDismissBox(
        modifier = Modifier
            .animateContentSize()
            .animateItem(),
        state = swipeState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    modifier = Modifier
                        .scale(iconScale)
                        .padding(horizontal = MaterialTheme.spacing.default),
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete_device),
                    tint = MaterialTheme.colorScheme.onError
                )
            }
        }
    ) {
        DeviceCard(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = MaterialTheme.spacing.default)
                .padding(vertical = MaterialTheme.spacing.small),
            hostName = device.hostName,
            ipAddress = device.ipAddress,
            isActive = device.isActive
        )
    }

    if (swipeState.currentValue == SwipeToDismissBoxValue.EndToStart) {
        LaunchedEffect(device.ipAddress) { currentOnDeleteDevice.value(device) }
        return
    }
}
