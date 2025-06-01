package com.luisfagundes.device.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
import com.luisfagundes.device.R
import com.luisfagundes.domain.model.Device

@Composable
fun DeviceListRoute(
    viewModel: DeviceListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DeviceListScreen(
        uiState = uiState
    )
}

@Composable
internal fun DeviceListScreen(
    uiState: DeviceListUiState
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        when (uiState) {
            is DeviceListUiState.Loading -> ScanningAnimation(
                modifier = Modifier.align(Alignment.Center)
            )

            is DeviceListUiState.Success -> FoundDevices(
                modifier = Modifier.fillMaxWidth(),
                devices = uiState.devices
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
private fun ScanningAnimation(
    modifier: Modifier = Modifier
) {
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
            dynamicProperties = dynamicProperties,
        )

        if (composition != null) {
            Text(
                text = stringResource(id = R.string.scanning_devices),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
internal fun FoundDevices(
    modifier: Modifier = Modifier,
    devices: List<Device>
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp),
    ) {
        stickyHeader {
            Text(
                text = pluralStringResource(
                    id = R.plurals.found_devices,
                    count = devices.size,
                    devices.size
                ),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        items(
            items = devices,
            key = { it.ipAddress }
        ) { device ->
            DeviceCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItem(),
                device = device
            )
        }
    }
}

@Composable
internal fun DeviceCard(
    modifier: Modifier = Modifier,
    device: Device
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = if (device.hostName.isEmpty() || device.hostName == device.ipAddress) {
                    stringResource(R.string.device_unknown)
                } else {
                    device.hostName
                },
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = device.ipAddress,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
