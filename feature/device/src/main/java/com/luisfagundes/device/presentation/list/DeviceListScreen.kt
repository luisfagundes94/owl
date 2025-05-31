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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
            is DeviceListUiState.Loading -> CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )

            is DeviceListUiState.Success -> DeviceListContent(
                modifier = Modifier.fillMaxWidth(),
                devices = uiState.devices
            )

            is DeviceListUiState.Error -> Text(
                modifier = Modifier.align(Alignment.Center),
                text = uiState.throwable.cause?.message ?: stringResource(
                    id = R.string.device_list_generic_error
                ),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
internal fun DeviceListContent(
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
                text = stringResource(
                    id = R.string.found_devices,
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
