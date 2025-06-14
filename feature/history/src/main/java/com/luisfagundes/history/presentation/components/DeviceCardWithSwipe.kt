package com.luisfagundes.history.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.lerp as lerpColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.util.lerp as lerpFloat
import com.luisfagundes.designsystem.component.DeviceCard
import com.luisfagundes.designsystem.theme.spacing
import com.luisfagundes.domain.model.Device
import com.luisfagundes.history.R
import com.luisfagundes.history.extensions.isEndToStartDirection
import com.luisfagundes.history.extensions.isSettledDirection

private const val SWIPE_THRESHOLD = 0.3f

@Composable
fun LazyItemScope.DeviceCardWithSwipe(
    device: Device,
    onDeleteDevice: (Device) -> Unit,
    modifier: Modifier = Modifier
) {
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
        modifier = modifier,
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
