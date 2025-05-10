package com.luisfagundes.device.presentation.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.luisfagundes.device.domain.model.Device
import com.luisfagundes.device.R

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
    when (uiState) {
        is DeviceListUiState.Loading -> CircularProgressIndicator(
            modifier = Modifier.fillMaxSize()
        )
        is DeviceListUiState.Success -> DeviceListContent(
            modifier = Modifier.fillMaxWidth(),
            devices = uiState.devices
        )
        is DeviceListUiState.Error -> Text(
            text = uiState.throwable.cause?.message ?: stringResource(
                id =R.string.device_list_generic_error
            ),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
internal fun DeviceListContent(
    modifier: Modifier = Modifier,
    devices: List<Device>
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(devices) { device ->
            Text(
                modifier = Modifier.padding(8.dp),
                text = "${device.hostName} (${device.ipAddress})",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}
