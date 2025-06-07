package com.luisfagundes.device.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.luisfagundes.designsystem.component.DeviceCard
import com.luisfagundes.designsystem.theme.spacing
import com.luisfagundes.device.R
import com.luisfagundes.domain.model.Device

@Composable
internal fun DeviceListRoute(viewModel: DeviceListViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DeviceListScreen(
        modifier = Modifier.fillMaxSize(),
        uiState = uiState,
        onRefresh = viewModel::scanDevices
    )
}

@Composable
private fun DeviceListScreen(
    modifier: Modifier = Modifier,
    uiState: DeviceListUiState,
    onRefresh: () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        when (uiState) {
            is DeviceListUiState.Loading -> ScanningAnimation(
                modifier = Modifier.align(Alignment.Center)
            )

            is DeviceListUiState.Success -> FoundDevices(
                modifier = Modifier.fillMaxWidth(),
                devices = uiState.devices,
                onRefresh = onRefresh
            )

            is DeviceListUiState.Error -> Text(
                modifier = Modifier.align(Alignment.Center),
                text = uiState.throwable.cause?.localizedMessage ?: stringResource(
                    id = R.string.device_list_generic_error
                ),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun ScanningAnimation(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.scanning))
        val progress by animateLottieCompositionAsState(composition)
        val dynamicProperties = rememberLottieDynamicProperties(
            rememberLottieDynamicProperty(
                property = LottieProperty.COLOR_FILTER,
                value = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                    MaterialTheme.colorScheme.primary.hashCode(),
                    BlendModeCompat.SRC_ATOP
                ),
                keyPath = arrayOf(
                    "**"
                )
            )
        )

        LottieAnimation(
            composition = composition,
            progress = { progress },
            dynamicProperties = dynamicProperties
        )

        if (composition != null) {
            Text(
                text = stringResource(id = R.string.scanning_devices),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = MaterialTheme.spacing.default)
            )
        }
    }
}

@Composable
internal fun FoundDevices(
    modifier: Modifier = Modifier,
    devices: List<Device>,
    onRefresh: () -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        stickyHeader {
            DeviceListHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.default),
                deviceCount = devices.size,
                onRefresh = onRefresh
            )
        }
        itemsIndexed(
            items = devices,
            key = { _, device -> device.ipAddress }
        ) { index, device ->
            DeviceCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.default)
                    .padding(vertical = MaterialTheme.spacing.small)
                    .animateItem(),
                hostName = device.hostName,
                ipAddress = device.ipAddress,
                isActive = device.isActive
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
private fun DeviceListHeader(
    modifier: Modifier = Modifier,
    deviceCount: Int,
    onRefresh: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = pluralStringResource(
                id = R.plurals.found_devices,
                count = deviceCount,
                deviceCount
            ),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        IconButton(onClick = onRefresh) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = stringResource(id = R.string.refresh_devices)
            )
        }
    }
}
