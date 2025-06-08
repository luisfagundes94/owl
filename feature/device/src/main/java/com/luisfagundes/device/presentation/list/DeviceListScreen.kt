package com.luisfagundes.device.presentation.list

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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.luisfagundes.common.permission.PermissionRequest
import com.luisfagundes.designsystem.component.DeviceCard
import com.luisfagundes.designsystem.theme.spacing
import com.luisfagundes.device.R
import com.luisfagundes.domain.model.Device

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun DeviceListRoute(viewModel: DeviceListViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val permissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)

    PermissionRequest(
        permissionState = permissionState,
        rationaleMessage = stringResource(R.string.location_permission_rationale),
        shouldShowRationale = uiState.shouldShowPermissionRationale,
        onGrant = {
            viewModel.getWifiSsid()
        },
        onRequestPermission = {
            permissionState.launchPermissionRequest()
            viewModel.hidePermissionRationale()
        },
        onDismiss = { dontAskAgain ->
            viewModel.onPermissionRationaleDismissed(dontAskAgain)
        }
    )

    DeviceListScreen(
        modifier = Modifier.fillMaxSize(),
        uiState = uiState,
        onRefresh = viewModel::scanDevices
    )
}

@Composable
private fun DeviceListScreen(
    uiState: DeviceListUiState,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        when {
            uiState.isLoading -> ScanningAnimation(
                modifier = Modifier.align(Alignment.Center)
            )

            uiState.devices.isNotEmpty() -> FoundDevices(
                devices = uiState.devices,
                wifiName = uiState.wifiName,
                onRefresh = onRefresh,
                modifier = Modifier.fillMaxWidth()
            )

            uiState.error != null -> Text(
                modifier = Modifier.align(Alignment.Center),
                text = uiState.error.cause?.localizedMessage ?: stringResource(
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
    devices: List<Device>,
    wifiName: String?,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        stickyHeader {
            DeviceListHeader(
                deviceCount = devices.size,
                onRefresh = onRefresh,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.default)
            )
        }
        item {
            WifiNetworkHeader(
                wifiName = wifiName,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.default)
                    .padding(bottom = MaterialTheme.spacing.small)
            )
        }
        itemsIndexed(
            items = devices,
            key = { _, device -> device.ipAddress }
        ) { index, device ->
            DeviceCard(
                hostName = device.hostName,
                ipAddress = device.ipAddress,
                isActive = device.isActive,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.default)
                    .padding(vertical = MaterialTheme.spacing.small)
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
private fun WifiNetworkHeader(wifiName: String?, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.verySmall),
        modifier = modifier
    ) {
        Text(
            text = wifiName ?: stringResource(id = R.string.unknown_wifi_name),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Icon(
            imageVector = Icons.Default.Wifi,
            contentDescription = stringResource(R.string.wifi_network),
            modifier = Modifier.scale(0.75f)
        )
    }
}

@Composable
private fun DeviceListHeader(
    deviceCount: Int,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
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
