package com.luisfagundes.history.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.luisfagundes.designsystem.component.DeviceCard
import com.luisfagundes.designsystem.theme.spacing
import com.luisfagundes.domain.model.Device
import com.luisfagundes.history.R
import com.luisfagundes.history.extensions.isEndToStartDirection
import com.luisfagundes.history.extensions.isSettledDirection
import androidx.compose.ui.graphics.lerp as lerpColor
import androidx.compose.ui.util.lerp as lerpFloat

private const val SWIPE_THRESHOLD = 0.3f

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
    ) {
        stickyHeader {
            Text(
                modifier = Modifier.padding(MaterialTheme.spacing.default),
                text = stringResource(R.string.device_history),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        items(devices, key = { it.ipAddress }) { device ->
            DeviceCardWithSwipe(
                device = device,
                onDeleteDevice = onDeleteDevice
            )
        }
    }
}

@Composable
private fun LazyItemScope.DeviceCardWithSwipe(
    device: Device,
    onDeleteDevice: (Device) -> Unit
) {
    val swipeState = rememberSwipeToDismissBoxState()
    val swipeProgress = if (swipeState.isSettledDirection) 0f else {
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
        modifier = Modifier.animateContentSize(),
        state = swipeState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Icon(
                    modifier = Modifier.scale(iconScale),
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
                .padding(vertical = MaterialTheme.spacing.small)
                .animateItem(),
            hostName = device.hostName,
            ipAddress = device.ipAddress,
            isActive = device.isActive
        )
    }

    if (swipeState.currentValue == SwipeToDismissBoxValue.EndToStart){
        LaunchedEffect(device.ipAddress) { onDeleteDevice(device) }
        return
    }
}

